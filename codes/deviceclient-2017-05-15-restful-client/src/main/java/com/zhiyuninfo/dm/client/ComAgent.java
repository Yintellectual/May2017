package com.zhiyuninfo.dm.client;

import com.zhiyuninfo.dm.client.utils.HttpUtils;
import com.zhiyuninfo.utility.domain.Attribute;
import com.zhiyuninfo.utility.domain.Device;
import com.zhiyuninfo.utility.domain.DeviceType;
import com.zhiyuninfo.utility.service.DeviceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("comAgent")
public class ComAgent extends Agent {
	@Autowired
	private DeviceService deviceService;
	
	private static final String COM_READER="modbus.exe";
	private List<Device> comDevices=new ArrayList<Device>();
	//private DeviceType comDeviceType = new DeviceType();
	private static String RTU_CONN_TYPE = "RTU";
	private static String TCP_CONN_TYPE = "TCP";
	protected Map<PLCAddress, DeviceAttribute> dataMapping = new HashMap<PLCAddress, DeviceAttribute>();

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/*public ComAgent(String dmURL,String deviceTypeName) {
		super(dmURL);
		
		this.deviceTypeName=deviceTypeName;
	}*/

	@Override
	public boolean recognizeDevice() {
		// 从平台获取最新的设备类型信息
		ResponseEntity<DeviceType> response = restTemplate.exchange(
				this.serviceSettings.getDMServiceURL() + "/devicetypes/type/" + this.params.getDeviceTypeName(),
				HttpMethod.GET, HttpUtils.genAuthHeaderForRest(null), DeviceType.class);
		this.deviceType = response.getBody();

		// Build the data mapping first in order to know the addresses to read
		// from COM
		this.buildDataMap();

		// 清空原有记录重新进行设备识别
		comDevices.clear();

		// 获取平台上已注册的所有同类型设备
		Device example = new Device();
		example.setType(this.deviceType.getName());
		List<Device> result = this.deviceService.getDevices(example);

		logger.info(String.format("Find %s COM devices!", result.size()));
		for (Device device : result) {
			if ((device.getComId() != null && !device.getComId().trim().equals("")) || 
					(device.getTcpDeviceIP()!=null && !"".equals(device.getTcpDeviceIP()))){
				this.comDevices.add(device);
			}
		}

		return true;
	}
	
	/**
	 * 将平台返回的数据转换成指令片数据
	 * @param comParityValue
	 * @return
	 */
	private String convertComParity(String comParityValue) {
		String result = "";
		switch(comParityValue){
		case "None Parity":
			result="N";
			break;
		case "Odd Parity":
			result="O";
			break;
		case "Even Parity":
			result="E";
			break;
		default:
			logger.error("[Warning]Unknown parity value:"+comParityValue);
			result=comParityValue;
		}
		return result;
	}
	
	/**
	 * 获取用户已经构建的数据映射关系从而为发送数据做准备
	 * 
	 * @param sourceDataFields
	 */
	protected void buildDataMap() {
		// 获取com口的配置
		logger.info(String.format("COM Settings:[comBaud=%s, comDataBits=%s,comParity=%s,comStopBit=%s]",
				this.deviceType.getComBaud(), this.deviceType.getComDataBits(), this.deviceType.getComParity(), this.deviceType.getComStopBit()));

		// Generate data mapping
		logger.info("Generate data mapping...");
		Map<PLCAddress, DeviceAttribute> currDataMapping = new HashMap<PLCAddress, DeviceAttribute>();
		boolean needReset = false;
		List<Attribute> attributes = deviceType.getCustomizedAttributes();
		for (Attribute attribute : attributes) {
			if (attribute.getPlcNo()!=null && attribute.getPlcNo()!=-1) {
				PLCAddress plcAddress=new PLCAddress(attribute.getPlcBeginAddress(), attribute.getPlcNo(),
						attribute.getPlcReadFunction(), attribute.getPlcNumber());
				
				currDataMapping.put(plcAddress,
						new DeviceAttribute(attribute.getName(), attribute.getType()));
				if (!(dataMapping.containsKey(plcAddress) && dataMapping.get(plcAddress).equals(currDataMapping.get(plcAddress)))) {
					needReset = true;//无需重新赋值
				}
			}
		}
		if (needReset) {
			dataMapping = currDataMapping;
		}

		// 获取设备类型中定义的最新同步频率信息
		if (deviceType.getSynchFrequency()!=0) {//
			if (deviceType.getSynchFrequency() != -1) {
				this.synchFrequency = deviceType.getSynchFrequency();
				logger.info(String.format("Set the synch frequency to %s for the device type \"%s\"",
						this.synchFrequency, this.deviceType.getName()));
			}
		}
	}

	@Override
	public void sendDeviceData() {
		if ("http".equals(params.getSendType())) {
			List<Device> devices=this.prepareSendData();
			for(Device device:devices){
				logger.info("com sendDeviceData：" + device.toString());
				ResponseEntity<Device> response = restTemplate.exchange(
						this.serviceSettings.getDMServiceURL()+"/devices/device/" + device.getId(),
						HttpMethod.POST, 
						HttpUtils.genAuthHeaderForRest(device), 
						Device.class);
				logger.info(String.format("Send attribute updates to cloud for the device \"%d\", return code=%d",
						device.getId(), response.getStatusCodeValue()));
			}
		}
	}
	
	@Override
	public void receiveDeviceData(){
	}
	
	private Map<PLCAddress, String> retrieveCOMData(Device device) {
		Map<PLCAddress, String> result = new HashMap<PLCAddress, String>();
		// 组装读取COM的命令
		String command = COM_READER + " ";
		if (RTU_CONN_TYPE.equalsIgnoreCase(this.deviceType.getConnectType())) {
			command += "modbusRtu" + " " + device.getComId() + " " + this.deviceType.getComBaud() + " " + convertComParity(this.deviceType.getComParity()) + " " + this.deviceType.getComDataBits()
					+ " " + this.deviceType.getComStopBit();
		} else if(TCP_CONN_TYPE.equalsIgnoreCase(this.deviceType.getConnectType())) {
			if (StringUtils.isEmpty(device.getTcpDeviceIP())) {
				logger.error("该设备" + this.deviceType.getName() + ", 设备未配置对应的TCP连接IP，请先通过平台配置... deviceId：" + device.getId());
				return null;
			}
			command += "modbusTcp" + " " + device.getTcpDeviceIP() + " " + this.deviceType.getTcpDevicePort();
		} else {
			logger.error("该设备" + this.deviceType.getName() + ", 未在平台配置通讯方式，请先配置通讯方式... ");
		}
		PLCAddress[] plcAddresses = new PLCAddress[this.dataMapping.size()];
		int i = 0;
		for (PLCAddress plcAddress : this.dataMapping.keySet()) {
			plcAddresses[i] = plcAddress;
			command += " " + this.dataMapping.get(plcAddress).type + " " + plcAddress.plcNo + " "
					+ plcAddress.plcReadFunction + " " + plcAddress.beginAddress + " " + plcAddress.plcNumber;
			i++;
		}

		logger.info("执行PLC读取程序:" + command);
		try {
			logger.info(command);
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = null;
			i = 0;
			while ((s = bufferedReader.readLine()) != null) {
				logger.info(s);
				if (s.indexOf("=") != -1) {
					String value=s.substring(s.indexOf("=") + 1).trim();
					if(!value.equals("E")){
						result.put(plcAddresses[i], value);
					}else{
						//logger.error(String.format("无法读出PLC的值[plcNo=%s,plcReadFunction=%s,plcBeginAddress=%,plcNumber=%s]", 
						//		plcAddresses[i].plcNo,plcAddresses[i].plcReadFunction,plcAddresses[i].beginAddress,plcAddresses[i].plcNumber));
					}
					
					i++;
				}
			}
			process.waitFor();

			// TODO: Need to deal with the situation that the PLC program can't read value from PLC equipment
			if (i != plcAddresses.length) {
				logger.error(
						String.format("[Warning] PLC program doesn't return the result enough:[expect=%s actual=%s]",
								plcAddresses.length, i));
			}
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}
	
	/**
	 * MQ消息发送机制下的发送数据组装
	 * @return
	 */
	@Override
	public List<Device> prepareSendData() {
		List<Device> result = new ArrayList<Device>();
		for (Device comDevice : comDevices) {
			// 从指定COM口根据data mapping读取数据
			Map<PLCAddress, String> comData = this.retrieveCOMData(comDevice);
			// Touch time & touch by will be sent every time
			comDevice.setTouchTime(System.currentTimeMillis());
			comDevice.setTouchBy("Agent-" + this.agentProperties.getProperty(AGENT_ID));
			
			Map<String, String> customizedAttrValues = new HashMap<String, String>();
			// Read attribute value from the json data object
			for (PLCAddress key : comData.keySet()) {
				DeviceAttribute deviceAttribute = this.dataMapping.get(key);
				logger.info("prepareDataMapping:" + key + "-" + this.dataMapping.get(key));
				if (deviceAttribute != null) {
					customizedAttrValues.put(deviceAttribute.name, comData.get(key));
				}
			}
			comDevice.setCustomizedAttrValues(customizedAttrValues);
			
			result.add(comDevice);
		}
		logger.info("平台上已注册的同类型com串口类型有" + comDevices.size() + "个。");
		return result;
	}
}
