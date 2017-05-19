package com.zhiyuninfo.dm.client;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages={"com.zhiyuninfo.dm.client","com.zhiyuninfo.utility.service","com.zhiyuninfo.debug"})
public class ApplicationConfig {
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	public PropertyPlaceholderConfigurer propertyConfigurer(){
		PropertyPlaceholderConfigurer propertyConfigurer=new PropertyPlaceholderConfigurer();
		propertyConfigurer.setLocation(new ClassPathResource("application.properties"));
		
		return propertyConfigurer;
	}
}
