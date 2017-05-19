package com.zhiyuninfo.dm.client;

import com.zhiyuninfo.dm.client.utils.AliHttpUtils;
import com.zhiyuninfo.dm.client.utils.HttpUtils;
import com.zhiyuninfo.utility.domain.Attribute;
import com.zhiyuninfo.utility.domain.Device;
import com.zhiyuninfo.utility.domain.DeviceType;
import com.zhiyuninfo.utility.service.DeviceService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue.ValueType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("weatherAgent")
public class WeatherAgent extends Agent {

	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 存放设备 及 设备对应的天气信息
	 */
	private List<Object[]> weatherDeviceDatas = new ArrayList<Object[]>();
	private final static String WEATHER_TYPE="天气监测";
	
	protected Map<String, DeviceAttribute> dataMapping = new HashMap<String, DeviceAttribute>();

	@Autowired
	private DeviceService deviceService;

	@Override
	public boolean recognizeDevice() {
		// 从平台获取最新的设备类型信息
		ResponseEntity<DeviceType> response = restTemplate.exchange(
				this.serviceSettings.getDMServiceURL()+"/devicetypes/type/" + WEATHER_TYPE,
				HttpMethod.GET, HttpUtils.genAuthHeaderForRest(null), DeviceType.class);
		logger.info(String.format("Response for getting the meta data of the device types: %d",
				response.getStatusCodeValue()));
		this.deviceType = response.getBody();// 获取设备类型中定义的最新同步频率信息
		
		// 获取平台上已注册的所有天气设备
		Device example = new Device();
		example.setType(this.deviceType.getName());
		List<Device> weatherDevices = deviceService.getDevices(example);
		logger.info(String.format("Find %s weather devices!", weatherDevices.size()));
		this.weatherDeviceDatas.clear();
		for (Device weatherDevice : weatherDevices) {
			JsonObject weatherData = this.retrieveWeatherData(weatherDevice.getLongitude(),
					weatherDevice.getLatitude());

			this.buildDataMap(weatherData.keySet());
			Object[] datas = new Object[2];
			datas[0] = weatherDevice;
			datas[1] = weatherData;
			this.weatherDeviceDatas.add(datas);
		}
		
		return true;
	}
	
	/**
	 * 将数据源数据结构发送至平台以便用户构建数据映射关系 同时获取用户已经构建的数据映射关系从而为发送数据做准备
	 * 
	 * @param sourceDataFields
	 */
	protected void buildDataMap(Collection<String> sourceDataFields) {
		// Send the source data fields to platform
		this.sendSourceDF(sourceDataFields);
		
		if (deviceType.getSynchFrequency() != 0 && deviceType.getSynchFrequency() != -1) {
			this.synchFrequency = deviceType.getSynchFrequency();
			logger.info(String.format("Set the synch frequency to %s for the device type \"%s\"", this.synchFrequency,
					this.deviceType.getName()));
		}

		// Generate data mapping
		dataMapping.clear();
		logger.info("Generate data mapping...");
		List<Attribute> attributes = deviceType.getCustomizedAttributes();
		for (Attribute attribute : attributes) {
			if (attribute.getInDataField() != null && !attribute.getInDataField().trim().equals("")) {
				dataMapping.put(attribute.getInDataField(),
						new DeviceAttribute(attribute.getName(), attribute.getType()));
			}
		}
	}

	private JsonObject retrieveWeatherData(double longitude, double latitude) {
		String path="/gps-to-weather";
		String method="GET";
		Map<String,String> headers=new HashMap<String,String>();
		headers.put("Authorization", "APPCODE "+Constants.APP_CODE);
		Map<String,String> querys=new HashMap<String,String>();
		querys.put("from", "5");
		querys.put("lat", String.valueOf(latitude));
		querys.put("lng", String.valueOf(longitude));
		querys.put("need3HourForcast", "0");
	    querys.put("needAlarm", "0");
	    querys.put("needHourData", "0");
	    querys.put("needIndex", "0");
	    querys.put("needMoreDay", "0");
	    
	    try{
	    	HttpResponse response=AliHttpUtils.doGet(Constants.HOST, path, method, headers, querys);
	    	String result=EntityUtils.toString(response.getEntity());
	    	logger.info(result);
	    	JsonReader reader = Json.createReader(new StringReader(result));
			return reader.readObject().getJsonObject("showapi_res_body").getJsonObject("now");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
		return null;
	}

	@Override
	public void sendDeviceData() {
		if ("http".equals(params.getSendType())) {
			List<Device> devices=this.prepareSendData();
			for(Device device:devices){
				ResponseEntity<Device> response = restTemplate.exchange(
						this.serviceSettings.getDMServiceURL()+"/devices/device/" + device.getId(), 
						HttpMethod.POST, 
						HttpUtils.genAuthHeaderForRest(device), 
						Device.class);
				logger.info(String.format("Send attribute updates to cloud for the weather device \"%d\", return code=%d",
						device.getId(), response.getStatusCodeValue()));
			}
		}
	}
	
	@Override
	public void receiveDeviceData(){
	}
	
	@Override
	public List<Device> prepareSendData() {
		List<Device> result = new ArrayList<Device>();
		
		for (Object[] weatherDeviceData : weatherDeviceDatas) {
			Map<String, String> customizedAttrValues = new HashMap<String, String>();
			// Read attribute value from the json data object
			//使用之前缓存的Device记录这样设备的地理位置可以保持不变
			Device weatherDevice = (Device)weatherDeviceData[0];
			weatherDevice.setTouchTime(System.currentTimeMillis());
			weatherDevice.setTouchBy("Agent-" + this.agentProperties.getProperty(AGENT_ID));
			JsonObject weatherData = (JsonObject)weatherDeviceData[1];
			for (String key : weatherData.keySet()) {
				DeviceAttribute deviceAttribute = this.dataMapping.get(key);
				if (deviceAttribute != null) {
					if (weatherData.get(key)!=null) {
						if(weatherData.get(key).getValueType() == ValueType.NUMBER){
							if(weatherData.getJsonNumber(key).isIntegral()){
								customizedAttrValues.put(deviceAttribute.name, String.valueOf(weatherData.getInt(key)));
							}else {
								customizedAttrValues.put(deviceAttribute.name, String.valueOf(weatherData.getJsonNumber(key).doubleValue()));
							}
						} else if (weatherData.get(key).getValueType() == ValueType.STRING){
							customizedAttrValues.put(deviceAttribute.name, weatherData.getString(key));
						}
					}
				}
			}
			weatherDevice.setCustomizedAttrValues(customizedAttrValues);
			
			result.add(weatherDevice);
		}
		
		return result;
	}
}
