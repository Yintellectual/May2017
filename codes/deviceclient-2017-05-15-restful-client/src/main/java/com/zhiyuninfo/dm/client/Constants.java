package com.zhiyuninfo.dm.client;

/**
 * 常量定义类
 * @author haipeng.cheng
 * @since 2016-11-12
 */
public class Constants {
	
	//client request path
	public final static String PATH_DEVICETYPES = "/devicetypes";
	public final static String PATH_DEVICES = "/devices/device";
	public final static String PATH_AGENTS_SETTINGS = "/agents/settings";
	public final static String PATH_AGENTS_AGENT = "/agents/agent";
	public final static String PATH_DEVICES_TYPE = "/devicetypes/type";
	public final static String PATH_DEVICES_SECURITY = "/devices/security";

	// APP KEY
	public final static String APP_KEY = "23488251";
	// APP密钥
	public final static String APP_SECRET = "79b610a5ae514e35ef019493bfec1186";
	// APPCode
	public final static String APP_CODE="1a1e7bd2bc034e6881706f874a2d703e";
	// API域名
	public final static String HOST = "http://ali-weather.showapi.com";
	
	//从服务器获取秘钥对应的报文key
	public final static String DEVICE_KEY = "deviceAuthKey";
	//设备存储对应的property key
	public final static String DEVICE_INIT_KEY = "deviceInitKey";
	
	//报文中对应的key
	public static final String DEVICE_ID = "deviceId";
	public static final String DEVICE_NAME = "deviceName";
	public static final String SIGN_METHOD = "signMethod";
	public static final String SIGN_MESSAGE = "signature";
}
