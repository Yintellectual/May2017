package com.zhiyuninfo.dm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSException;
import com.zhiyuninfo.dm.client.utils.HttpUtils;
import com.zhiyuninfo.utility.domain.Attachment;
import com.zhiyuninfo.utility.domain.Attribute;
import com.zhiyuninfo.utility.domain.Device;
import com.zhiyuninfo.utility.domain.DeviceType;
import com.zhiyuninfo.utility.service.AttachmentService;

@Service("deviceAgent")
public class DeviceAgent extends Agent {
	private File deviceDataFile = null;
	//private boolean randomData = false;
	private Map<String, DeviceAttribute> inDataMapping = new HashMap<String, DeviceAttribute>();
	private Map<String, DeviceAttribute> outDataMapping = new HashMap<String,DeviceAttribute>();
	private Device deviceCurrentData;

	@Autowired
	private AttachmentService attachmentService;
	
	/**
	 * Recognize the device type according to the device type configuration in
	 * platform
	 */
	@Override
	public boolean recognizeDevice() {
		ResponseEntity<List<DeviceType>> response = restTemplate.exchange(
				this.serviceSettings.getDMServiceURL()+"/devicetypes", 
				HttpMethod.GET, 
				HttpUtils.genAuthHeaderForRest(null), 
				new ParameterizedTypeReference<List<DeviceType>>() {
				});
		logger.info(String.format("Response for getting the meta data of the device types: %d", response.getStatusCodeValue()));
		List<DeviceType> deviceTypes = response.getBody();

		// Try to recongnize the device
		for (DeviceType deviceType : deviceTypes) {
			// Locate the data file
			if (deviceType.getDataFile()==null) {
				logger.warn(String.format(
						"No data file defined for the device type \"%s\", please define the client data file in platform!",
						deviceType.getName()));
				continue;
			} else {
				deviceDataFile = new File(deviceType.getDataFile());
				if (!deviceDataFile.exists()) {
					logger.warn(
							String.format("Can't find the client data file: %s!", deviceType.getDataFile()));
					continue;
				} else {
					this.deviceType=deviceType;
					logger.info("Recognize the device type:" + this.deviceType.getName());

					Properties temp = new Properties();
					try {
						FileInputStream fInput = new FileInputStream(deviceDataFile);
						temp.load(new InputStreamReader(fInput));
						List<String> dataFields = new ArrayList<String>();
						for (Object dataField : temp.keySet()) {
							dataFields.add((String) dataField);
						}

						this.buildDataMap(dataFields);
					} catch (IOException e) {
						logger.error("Can't read device data file!", e);
					}

					// Register the device
					String deviceName = this.deviceType.getName() + "-" + this.agentProperties.getProperty(AGENT_ID);
					while (this.deviceId == -1) {
						// Try to retrieve the device id from agent property
						// file
						if (this.agentProperties.containsKey(DEVICE_ID)) {
							this.deviceId = Integer.parseInt(this.agentProperties.getProperty(DEVICE_ID));
							// Check if the device id is existed in platform
							ResponseEntity<Device> qryResponse = restTemplate.exchange(
									this.serviceSettings.getDMServiceURL()+"/devices/device/" + this.deviceId, 
									HttpMethod.GET, 
									HttpUtils.genAuthHeaderForRest(null), 
									Device.class);
							if (response.getStatusCodeValue() != 200) {
								this.deviceId = -1;
								this.agentProperties.remove(DEVICE_ID);
								this.saveProperties();
								continue;
							} else {
								this.deviceCurrentData = qryResponse.getBody();
							}
						} else {
							// Register Device
							Device device = new Device();
							device.setType(this.deviceType.getName());
							device.setName(deviceName);
							device.setTouchBy("Agent-" + this.agentProperties.getProperty(AGENT_ID));
							device.setTouchTime(System.currentTimeMillis());
							ResponseEntity<Device> addResponse = restTemplate.exchange(
									this.serviceSettings.getDMServiceURL()+"/devices/device", 
									HttpMethod.POST, 
									HttpUtils.genAuthHeaderForRest(device), 
									Device.class);
							device = addResponse.getBody();
							logger.info("Register as a new device with id:" + device.getId());

							this.deviceId = device.getId();
							this.agentProperties.put(DEVICE_ID, String.valueOf(this.deviceId));
							this.saveProperties();
							this.deviceCurrentData=device;
						}
					}

					return true;
				}
			}
		}

		logger.warn("Can't recognize the device type.");
		if (this.deviceId != -1) { // Clean the device id
			this.deviceId = -1;
			this.agentProperties.remove(DEVICE_ID);
			this.saveProperties();
		}

		return false;
	}
	
	/**
	 * 将数据源数据结构发送至平台以便用户构建数据映射关系 同时获取用户已经构建的数据映射关系从而为发送数据做准备
	 * 
	 * @param sourceDataFields
	 */
	protected void buildDataMap(Collection<String> sourceDataFields) {
		// Send the source data fields to platform
		this.sendSourceDF(sourceDataFields);

		// Generate data mapping
		Map<String, DeviceAttribute> inTemp = new HashMap<String, DeviceAttribute>();
		Map<String, DeviceAttribute> outTemp=new HashMap<String,DeviceAttribute>();
		boolean inNeedReset = false;
		boolean outNeedReset = false;
		logger.info("Generate data mapping...");
		List<Attribute> attributes = deviceType.getCustomizedAttributes();
		for (Attribute attribute : attributes) {
			if (attribute.getInDataField()!=null && !attribute.getInDataField().trim().equals("")) {
				inTemp.put(attribute.getInDataField(),
						new DeviceAttribute(attribute.getName(), attribute.getType()));
				if (!(inDataMapping.containsKey(attribute.getInDataField()) &&
						inTemp.get(attribute.getInDataField()).equals(inDataMapping.get(attribute.getInDataField())))) {
					inNeedReset = true;
				}
			}
			if(attribute.getOutDataField()!=null && !attribute.getOutDataField().trim().equals("")){
				outTemp.put(attribute.getOutDataField(), new DeviceAttribute(attribute.getName(),attribute.getType()));
				if(!outDataMapping.containsKey(attribute.getOutDataField()) || 
						!outTemp.get(attribute.getOutDataField()).equals(outDataMapping.get(attribute.getOutDataField()))){
					outNeedReset=true;
				}
			}
		}
		if (inNeedReset) {
			inDataMapping = inTemp;
		}
		if(outNeedReset){
			outDataMapping=outTemp;
		}

		// 获取设备类型中定义的最新同步频率信息
		if (deviceType.getSynchFrequency()!=0) {
			if (deviceType.getSynchFrequency() != -1) {
				this.synchFrequency = deviceType.getSynchFrequency();
				logger.info(String.format("Set the synch frequency to %s for the device type \"%s\"",
						this.synchFrequency, this.deviceType.getName()));
			}
		}
	}
	
	private void genCustomizedAttrValue(Map<String, String> customizedAttrValues,
			DeviceAttribute attribute, String value) {
		if (attribute.type.equals(DeviceAttribute.FILE_TYPE)) {
			try {
				logger.info(String.format("准备上传设备数据文件[attribute=%s, file=%s]",
						attribute.name, value));
				File localFile=new File(value);
				long time=System.currentTimeMillis();
				Attachment attachment=this.attachmentService.addAttachment(
						"deviceclient", String.valueOf(this.deviceId),
						attribute.name,value,IOUtils.readFully(new FileInputStream(localFile),
								(int)localFile.length()));
				logger.info("上传文件完毕，耗时："+(System.currentTimeMillis()-time)/1000);
				String fileURL=attachment.getUri();
				logger.info("阿里云文件:" + fileURL);
				customizedAttrValues.put(attribute.name, fileURL);
			} catch (OSSException | IOException e) {
				e.printStackTrace();
			}
		}else{
			customizedAttrValues.put(attribute.name, generateRandomValue(attribute, value));
		}
	}

	/**
	 * Send device data to platform
	 */
	@Override
	public void sendDeviceData() {
		if ("http".equals(params.getSendType())) {
			List<Device> sendDevices=this.prepareSendData();
			logger.info("device sendDeviceData:" + sendDevices.get(0).toString());
			ResponseEntity<Device> response = restTemplate.exchange(
					this.serviceSettings.getDMServiceURL()+"/devices/device/" + this.deviceId, 
					HttpMethod.POST, 
					HttpUtils.genAuthHeaderForRest(sendDevices.get(0)), 
					Device.class);
			logger.info(String.format("Send attribute updates to cloud for the device \"%d\", return code=%d",
					this.deviceId, response.getStatusCodeValue()));
		}
	}
	
	@Override
	public void receiveDeviceData() {
		ResponseEntity<Device> response = restTemplate.exchange(
				this.serviceSettings.getDMServiceURL()+"/devices/device/" + this.deviceId, 
				HttpMethod.GET, 
				HttpUtils.genAuthHeaderForRest(null), 
				Device.class);
		this.deviceCurrentData=response.getBody();
		
		// Read machine data from the properties file
		Properties machineDatas = new Properties();
		try {
			FileInputStream fInput = new FileInputStream(this.deviceDataFile);
			machineDatas.load(new InputStreamReader(fInput));

			// 使用最新的设备数据执行写数据操作
			boolean machineDataUpdated = false;
			for (String machineDataKey : this.outDataMapping.keySet()) {
				DeviceAttribute deviceAttribute = this.outDataMapping.get(machineDataKey);
				String value = this.deviceCurrentData.getAttributeValue(deviceAttribute.name);
				if (value != null) {
					if (deltaSynch || this.deviceType.getDeltaSynch()) {
						String machineValue = machineDatas.getProperty(machineDataKey);
						if (!value.equals(machineValue)) {
							machineDatas.put(machineDataKey, value);
							machineDataUpdated = true;
							logger.info(String.format("Update the machine data %s to %s", machineDataKey, value));
						}
					} else {
						machineDatas.put(machineDataKey, value);
						machineDataUpdated = true;
						logger.info(String.format("Update the machine data %s to %s", machineDataKey, value));
					}
				}
			}
			if (machineDataUpdated) {
				FileOutputStream fOutput = new FileOutputStream(this.deviceDataFile);
				machineDatas.store(fOutput, "Updated by agent");
				fOutput.close();
			}

			fInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Device> prepareSendData() {
		List<Device> result = new ArrayList<Device>();
		
		//先执行读数据操作
		// Touch time & touch by will be sent every time
		this.deviceCurrentData.setTouchTime(System.currentTimeMillis());
		this.deviceCurrentData.setTouchBy("Agent-" + this.agentProperties.getProperty(AGENT_ID));
		Map<String, String> customizedAttrValues = new HashMap<String, String>();
		// Read machine data from the properties file
		Properties machineDatas = new Properties();
		try {
			FileInputStream fInput = new FileInputStream(this.deviceDataFile);
			machineDatas.load(new InputStreamReader(fInput));
			
			for(String machineDataKey:this.inDataMapping.keySet()){
				// Get the relevant device attribute name
				DeviceAttribute deviceAttribute = this.inDataMapping.get(machineDataKey);
				String value=machineDatas.getProperty(machineDataKey);
				if (value != null) {
					if (deltaSynch || this.deviceType.getDeltaSynch()) { // Only update when there is an attribute change
						logger.info("仅仅发送更改了的数据！");
						String attributeValue = deviceCurrentData.getAttributeValue(deviceAttribute.name);
						if (attributeValue != null && deviceAttribute.type.equals(DeviceAttribute.FILE_TYPE)) { // 特殊处理，去掉前面的URL只要文件名
																												// TODO
																												// 需要重构
							attributeValue = attributeValue.substring(attributeValue.lastIndexOf("/") + 1);
							logger.info("文件字段特殊处理：" + attributeValue);
						}
						if (deviceCurrentData.getName() == null || !value.equals(attributeValue)) {
							this.genCustomizedAttrValue(customizedAttrValues, deviceAttribute, value);
						}
					} else {
						this.genCustomizedAttrValue(customizedAttrValues, deviceAttribute, value);
					}
				} else {
					// 用户定义的新的机器地址，需要告知机器本地程序进行读值
					logger.info(String.format(
							"The key %s is NOT defined in the machine data file. Will add it for the attribute %s.",
							machineDataKey, deviceAttribute.name));
				}
			}
			fInput.close();
		} catch (IOException e) {
			logger.error("Can't read device data file!", e);
		}
		this.deviceCurrentData.setCustomizedAttrValues(customizedAttrValues);
		
		
		result.add(this.deviceCurrentData);
		return result;
	}
	
	/**
	 * 对指定的数值做随机化处理，该方法主要用于测试或演示
	 * 
	 * @param value
	 */
	private String generateRandomValue(DeviceAttribute attribute, String value) {
		if (this.params.getRandomData()) {
			if (attribute.type.equals(DeviceAttribute.INT_TYPE)) {
				logger.info("Generate randome value for " + value);
				value = String.valueOf((int) (Integer.parseInt(value) * Math.random()));
				logger.info("New value:" + value);
			}else if(attribute.type.equals(DeviceAttribute.DOUBLE_TYPE)){
				logger.info("Generate randome value for " + value);
				value = String.valueOf(Double.parseDouble(value) * Math.random());
				logger.info("New value:" + value);
			}
		}

		return value;
	}
}
