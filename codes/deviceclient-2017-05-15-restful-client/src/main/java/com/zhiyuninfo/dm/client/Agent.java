package com.zhiyuninfo.dm.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.zhiyuninfo.dm.client.utils.HttpUtils;
import com.zhiyuninfo.dm.client.utils.MQSender;
import com.zhiyuninfo.utility.domain.AgentSettings;
import com.zhiyuninfo.utility.domain.Device;
import com.zhiyuninfo.utility.domain.DeviceType;
import com.zhiyuninfo.utility.service.AgentService;
import com.zhiyuninfo.utility.service.ServiceSettings;

@Component
public abstract class Agent extends Thread {
	@Autowired
	protected InputParams params;
	@Autowired
	protected RestTemplate restTemplate;
	@Autowired
	protected ServiceSettings serviceSettings;
	@Autowired
	private AgentService agentService;
	@Autowired
	protected MQSender asynSender;
	
	protected static final String AGENT_ID = "agentId";
	protected static final String DEVICE_ID = "deviceId";
	protected static final String UID = "uniqueId";
	private static final String SERVICE_URL="serviceURL";

	private static final String agentPropertyFile = "deviceagent.ini";
	private int agentSynchFrequency = Integer.MAX_VALUE;
	public int agentActualSynchFrequency = 300;
	private int heartBeatRate = 3600;
	private MQThread sendDeviceData=null;

	protected int synchFrequency = Integer.MAX_VALUE;
	protected boolean deltaSynch = true;
	protected DeviceType deviceType = null;
	//protected String deviceTypeName = null;
	protected int deviceId = -1;
	protected Properties agentProperties = new Properties();
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Save agent properties into the property file
	 */
	public void saveProperties() {
		try {
			agentProperties.store(new FileOutputStream(agentPropertyFile), null);
		} catch (FileNotFoundException e) {
			logger.error("Can't save properties!", e);
		} catch (IOException e) {
			logger.error("Can't save properties!", e);
		}
	}

	/**
	 * Load agent settings from platform
	 */
	public void loadAgentSettings() {
		// Read properties from the config file
		try {
			FileInputStream fInput = new FileInputStream(agentPropertyFile);
			agentProperties.load(new InputStreamReader(fInput));
		} catch (IOException e) {
			logger.error("Can't load properties!", e);
		}
		String serviceURL=agentProperties.getProperty(SERVICE_URL);
		if(serviceURL!=null && !serviceURL.equals(serviceSettings.getDMServiceURL())){ //优先使用配置文件里设置的Service URL
			logger.info(String.format("使用配置文件定义的Service URL[old=%s new=%s]", serviceSettings.getDMServiceURL(),serviceURL));
			serviceSettings.setDMServiceURL(serviceURL);
		}else if(serviceURL==null){ //将系统使用的Service URL写入配置文件
			agentProperties.put(SERVICE_URL, serviceSettings.getDMServiceURL());
			this.saveProperties();
		}
		
		// Load general agent settings

		AgentSettings object = agentService.loadAgentSettings();
		if (this.heartBeatRate != object.getHeartBeatRate()) {
			logger.info(String.format("Change the heart beat rate from %ss to %ss!", this.heartBeatRate,
					object.getHeartBeatRate()));
			this.heartBeatRate = object.getHeartBeatRate();
		}
		/*if (!Agent.agentPropertyFile.equals(object.getAgentPropertyFile())) {
			logger.info(String.format("Change the agent property file from %s to %s!", Agent.agentPropertyFile,
					object.getAgentPropertyFile()));
			Agent.agentPropertyFile = object.getAgentPropertyFile();
		}*/
		if (this.deltaSynch != object.isDeltaSynch()) {
			logger.info(String.format("Change delta synch from %s to %s!", this.deltaSynch,
					object.isDeltaSynch()));
			this.deltaSynch = object.isDeltaSynch();
		}
		
		// Load agent specific settings
		if (agentProperties.containsKey("agentId")) {
			com.zhiyuninfo.utility.domain.Agent agent=
					agentService.loadAgent(Integer.parseInt(agentProperties.getProperty("agentId")));
			this.agentSynchFrequency = agent.getSynchFrequency();
		}
	}

	/**
	 * Register in platform as an agent
	 */
	public void register() {
		if (!agentProperties.containsKey(AGENT_ID)) {
			try {
				// 生成Agent的UUID
				UUID uid = UUID.randomUUID();
				com.zhiyuninfo.utility.domain.Agent agent = new com.zhiyuninfo.utility.domain.Agent();
				agent.setStatus("running");
				agent.setIpAddress(InetAddress.getLocalHost().getHostAddress());
				agent.setUid(uid.toString());
				agent.setType(this.getClass().getSimpleName());
				agent.setActualSynchFrequency(Integer.MAX_VALUE);
				agent.setSynchFrequency(Integer.MAX_VALUE);
				// Register the new agent
				// TODO: Use AgentService here
				ResponseEntity<com.zhiyuninfo.utility.domain.Agent> response = restTemplate.exchange(
						this.serviceSettings.getDMServiceURL()+"/agents/agent", 
						HttpMethod.POST,
						HttpUtils.genAuthHeaderForRest(agent), 
						com.zhiyuninfo.utility.domain.Agent.class);
				agent = response.getBody();
				agentProperties.put(AGENT_ID, String.valueOf(agent.getId()));
				agentProperties.put(UID, uid.toString());

				this.saveProperties();
				logger.info(String.format("Register to platform: Reponse=%s Agent ID=%s UUID=%s", response.getStatusCodeValue(),
						agentProperties.getProperty(AGENT_ID), agentProperties.getProperty(UID)));
			} catch (UnknownHostException e) {
				logger.error("Can't get ip address!", e);
			}
		} else {
			// Check if the agent id is existed in platform
			com.zhiyuninfo.utility.domain.Agent agent=null;
			try{
				agent=agentService.loadAgent(Integer.parseInt(agentProperties.getProperty("agentId")));
			}catch(RestClientException e){
				e.printStackTrace();
			}
			
			if(agent==null || !agent.getUid().equals(agentProperties.getProperty(UID))){ // 检查这个Agent记录是否存在以及它的uid是否和Agent uid匹配
				agentProperties.remove(AGENT_ID);
				this.saveProperties();
				// Re-register
				register();
			}
		}
	}

	/**
	 * Send heart beat to platform
	 */
	public void sendHeartBeat() {
		// Calculate the actual frequency of this agent
		this.agentActualSynchFrequency = Math.min(this.heartBeatRate, this.synchFrequency);
		this.agentActualSynchFrequency = Math.min(this.agentActualSynchFrequency, this.agentSynchFrequency);

		try {
			com.zhiyuninfo.utility.domain.Agent agent = new com.zhiyuninfo.utility.domain.Agent();
			agent.setActualSynchFrequency(Integer.MAX_VALUE);
			agent.setSynchFrequency(Integer.MAX_VALUE);
			agent.setIpAddress(InetAddress.getLocalHost().getHostAddress());
			agent.setStatus("running");
			// Update the agent status
			agent.setId(Integer.parseInt(agentProperties.getProperty("agentId")));
			ResponseEntity<com.zhiyuninfo.utility.domain.Agent> response = restTemplate.exchange(
					this.serviceSettings.getDMServiceURL()+"/agents/agent/" + agentProperties.getProperty("agentId"), 
					HttpMethod.POST, 
					HttpUtils.genAuthHeaderForRest(agent), 
					com.zhiyuninfo.utility.domain.Agent.class);
			logger.info(String.format("Send the heart beat to platform: Response=%s", response.getStatusCodeValue()));
			if (response.getStatusCodeValue() != 200 || !(response.getBody().getUid()
					.equals(agentProperties.getProperty(UID)))) {
				// Can't find the relevant agent in platform, re-register it!
				this.register();
			}
		} catch (UnknownHostException e) {
			logger.error("Can't get ip address!", e);
		}
	}
	
	protected void sendSourceDF(Collection<String> sourceDataFields) {
		// Send the source data fields to platform
		List<String> dataFields = new ArrayList<String>();
		for (String dataField : sourceDataFields) {
			dataFields.add(dataField);
		}
		ResponseEntity<Boolean> response = restTemplate.exchange(
				this.serviceSettings.getDMServiceURL()+"/devicetypes/type/updatefields/" + this.deviceType.getName(), 
				HttpMethod.POST, 
				HttpUtils.genAuthHeaderForRest(dataFields), 
				Boolean.class);
		logger.info(String.format("Response for sending the data file structure for the device type \"%s\": %d",
				this.deviceType.getName(), response.getStatusCodeValue()));
	}

	/**
	 * Recognize the device type, different agent type might have different
	 * recognization method.
	 * 
	 * platform
	 */
	public abstract boolean recognizeDevice();

	/**
	 * Assemble latest data before sendDeviceData to 
	 * 
	 * platform
	 */
	public abstract List<Device> prepareSendData();
	
	/**
	 * Send device data to platform, different agent type might have different
	 * method for sending data
	 * 
	 */
	public abstract void sendDeviceData();
	
	/**
	 * Receive device data from platform, different agent type might have different
	 * method for retrieving data
	 * 
	 */
	public abstract void receiveDeviceData();

	@Override
	public void run() {
		// Load the agent settings first
		this.loadAgentSettings();
		// Register the agent in the platform
		this.register();

		// Process the 1st round before the loop
		this.processOneRound();

		//启动独立的MQ Thread定时发送设备数据
		//即使在主线程因为读取平台API受阻也不影响定时发送数据
		if ("mq".equals(params.getSendType())) { 
			this.sendDeviceData=new MQThread(this,this.asynSender);
			this.sendDeviceData.start();
		}
		
		long invokeDuration=0;
		while (true) {
			try {
				// Pause several seconds according to the actual synch frequency
				// 先进行暂停，这样如果后面方法执行抛出异常不至于执行的过快
				logger.info("Sych Frequency:" + this.agentActualSynchFrequency);
				//扣除调用时间开销
				long sleepTime=this.agentActualSynchFrequency * 1000-invokeDuration;
				logger.info("Sleep Time:" + sleepTime);
				if(sleepTime>0){
					Thread.sleep(sleepTime);
				}
				logger.info("Properties:" + this.agentProperties);
				invokeDuration=System.currentTimeMillis();
				this.processOneRound();
				invokeDuration=System.currentTimeMillis()-invokeDuration;
			} catch (Exception e) {
				logger.error("Unexpected Error:", e);
			}
		}
		
	}

	private void processOneRound() {
		// Retrieve the latest agent settings
		this.loadAgentSettings();
		if (this.recognizeDevice()) {
			this.sendDeviceData();
			this.receiveDeviceData();
		}
		this.sendHeartBeat();
	}
}
