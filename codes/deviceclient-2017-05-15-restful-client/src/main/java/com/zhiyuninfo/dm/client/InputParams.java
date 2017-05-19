package com.zhiyuninfo.dm.client;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备启动时入参
 * @author haipeng.cheng
 * @since 2017-01-14
 */
@Setter
@Getter
@Service("params")
public class InputParams {
	//消息队列地址
	private String brokerUrl;
	
	//
	private boolean randomData;
	
	//客户端与服务端的发送方式-默认 是 http交互方式
	private String sendType = "http";
	
	//设备类型
	private String deviceTypeName;
	
	public boolean getRandomData() {
		return this.randomData;
	}
}
