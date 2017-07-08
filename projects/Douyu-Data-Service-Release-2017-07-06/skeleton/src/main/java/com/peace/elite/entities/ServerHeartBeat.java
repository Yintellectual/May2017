package com.peace.elite.entities;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerHeartBeat{

	private long id;
	private DouyuMessageType type = DouyuMessageType.keeplive;
	private long tick; 
	
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static ServerHeartBeat getInstance(Map<String, String> message){
		return mapper.convertValue(message, ServerHeartBeat.class);
	}
	
}
