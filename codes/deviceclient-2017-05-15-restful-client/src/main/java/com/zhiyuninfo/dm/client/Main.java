package com.zhiyuninfo.dm.client;

import com.zhiyuninfo.utility.service.ServiceSettings;
import com.zhiyuninfo.debug.DebugRestfulClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public class Main {
    private static String brokerUrl = "tcp://www.zhiyuninfo.com:61616/";
    private static boolean randomData = false;
    private static String sendType = "http"; //默认 是 http交互方式
    private static String comDeviceType = null;
    private static String serviceUrl = null;
    private static boolean isWeatherAgent = false;
    private static boolean isComAgent = false;
    private static ApplicationContext context;

    public static void main(String[] args) throws Exception {
        //System.setProperty("java.net.preferIPv6Addresses", "true");
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		
        //Agent agent = processArgs(args);
		((DebugRestfulClient)context.getBean("debugRestfulClient")).debugDeviceTypes(args);
        //agent.start();

        //agent.join();
		

    }

    private static Agent processArgs(String[] args) {
        InputParams params = (InputParams) context.getBean("params");
        readConf();
        for(int i=0;i<args.length;i=i+2){
			if(args[i].equals("-mq")){
				sendType="mq";
				brokerUrl = args[i+1];
			}else if(args[i].equals("-http")){
				sendType="http";
			}else if(args[i].equals("-host")){
				context.getBean(ServiceSettings.class).setDMServiceURL(args[i+1]);
			}else if(args[i].equals("-random")){
				randomData=Boolean.parseBoolean(args[i+1]);
			}else if(args[i].equals("-help")){
				System.out.println("Supported Parameters: -type <agent type> -host <host> -random <if send random data>");
			}else if(args[i].equals("-type")){
				if(args[i+1].equals("weather")){
					isWeatherAgent=true;
				}else if(args[i+1].equals("com")){
					isComAgent=true;
					comDeviceType=args[i+2];
				}
			}
		}
        params.setSendType(sendType);
        params.setRandomData(randomData);
        params.setDeviceTypeName(comDeviceType);
        params.setBrokerUrl(brokerUrl);

        Agent agent;
        if (isWeatherAgent) {
            //return new WeatherAgent(dmURL);
            agent = (Agent) context.getBean("weatherAgent");
        } else if (isComAgent) {
            //return new ComAgent(dmURL,comDeviceType);
            agent = (Agent) context.getBean("comAgent");
        } else {
            //return new DeviceAgent(dmURL,randomData);
            agent = (Agent) context.getBean("deviceAgent");
        }
        return agent;
    }

    private static void readConf() {
        FileInputStream inputStream = null;
        try {
            String path = getPath();
            Properties properties = new Properties();
            //edit 1
			//remove path, because the default start.conf is in the root directory of class path
			File file = new File("start.conf");
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
            brokerUrl = properties.getProperty("agent.broker.url");
            sendType = properties.getProperty("agent.send.type");
            context.getBean(ServiceSettings.class).setDMServiceURL(properties.getProperty("agent.dm.service.url"));
            randomData = Boolean.valueOf(properties.getProperty("agent.random.data"));
            String agentType = properties.getProperty("agent.type");
            if("weather".equalsIgnoreCase(agentType)) {
                isWeatherAgent = true;
            }else if("com".equalsIgnoreCase(agentType)) {
                isComAgent=true;
            }
            comDeviceType = properties.getProperty("agent.com.device.type");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getPath() {
        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }
}
