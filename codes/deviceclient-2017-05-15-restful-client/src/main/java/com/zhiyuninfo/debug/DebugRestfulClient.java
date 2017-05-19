package com.zhiyuninfo.debug;

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
import org.springframework.web.client.HttpClientErrorException;
@Component
public class DebugRestfulClient{
	
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
	

	protected int synchFrequency = Integer.MAX_VALUE;
	protected boolean deltaSynch = true;
	protected DeviceType deviceType = null;
	//protected String deviceTypeName = null;
	protected int deviceId = -1;
	protected Properties agentProperties = new Properties();
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public void debugDeviceTypes(String...args){
			String url="";
			for(String arg:args){
				url+=arg;
			}
			
			try{
			ResponseEntity<String> response = restTemplate.exchange(
					url,
					HttpMethod.GET, 
					HttpUtils.genAuthHeaderForRest(null),
					String.class);
			System.out.println(response.getBody());
			}catch(HttpClientErrorException e){
			ResponseEntity<String> response = restTemplate.exchange(
					url,
					HttpMethod.POST, 
					HttpUtils.genAuthHeaderForRest(null),
					String.class);
			System.out.println(response.getBody());
			}
	}
}
