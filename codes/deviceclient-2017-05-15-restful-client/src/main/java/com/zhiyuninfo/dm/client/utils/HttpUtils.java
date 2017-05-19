package com.zhiyuninfo.dm.client.utils;

import javax.ws.rs.client.Invocation.Builder;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


/**
 * 公共类
 * @author haipeng.cheng
 * @since 2016-11-20
 *
 */
public class HttpUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HttpEntity<String> genHeaderForRest(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(body, headers);
		return entity;
	}

	public static HttpEntity<String> genAuthHeaderForRest(Object body) {
		return genAuthHeaderForRest(body, MediaType.APPLICATION_JSON);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HttpEntity<String> genAuthHeaderForRest(Object body, MediaType contentType) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", genAuthorization());
		headers.setContentType(contentType);
		HttpEntity entity = new HttpEntity(body, headers);
		return entity;
	}
	
	/**
	 * 所有抽象类 或 子类调服务端请求需要认证的公共rest请求入口
	 * @param Builder builder
	 * @return
	 */
	public static Builder addAuthForJeser(Builder builder) {
		builder.header("Authorization", genAuthorization());
		return builder;
	}
	
    public static String genAuthorization() {
		String loginInfo = "deviceclient:zhiyun888888";
		String authorization = "Basic " + Base64.encodeBase64String(loginInfo.getBytes());
		return authorization;
	}
}
