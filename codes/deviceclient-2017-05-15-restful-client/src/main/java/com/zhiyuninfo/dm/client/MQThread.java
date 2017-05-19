package com.zhiyuninfo.dm.client;

import com.zhiyuninfo.dm.client.utils.MQSender;
import com.zhiyuninfo.utility.domain.Device;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class MQThread extends Thread {

	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String UPDATE_DEVICE_QUEUE = "send.updateDevice.queue";
	
	private MQSender asynSender;
	
	private Agent agent;
	
	protected MQThread(Agent agent, MQSender asynSender){
		this.agent=agent;
		this.asynSender=asynSender;
	}
	
	@Override
	public void run() {
		long invokeDuration=0;
		while (true) {
			try {
				logger.info("**sendDeviceDataToQueue -- Synch Frequency:" + agent.agentActualSynchFrequency);
				//扣除调用时间开销
				long sleepTime=agent.agentActualSynchFrequency * 1000-invokeDuration;
				logger.info("Sleep Time:" + sleepTime);
				if(sleepTime>0){
					Thread.sleep(sleepTime);
				}
				
				invokeDuration=System.currentTimeMillis();
				List<Device> devices = agent.prepareSendData();
				if (devices!=null && devices.size()>0) {
					for (Device device : devices) {
						this.asynSender.asynSend(UPDATE_DEVICE_QUEUE, device);
					}
					logger.info("设备端程序 一个频次 发送数据至 队列 完毕！数据量为：" + devices.size());
				} else {
					logger.info("设备端程序发送数据信息至MQ时，无最新数据 或 最新数据组装失败、、、 ");
				}
				invokeDuration=System.currentTimeMillis()-invokeDuration;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("设备端程序 发送数据至 队列出现异常", e);
			}
		}
	}	
}
