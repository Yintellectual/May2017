package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperChatMessage{
	
	private long id;
	private String type;
	private long rid;
	private long gid;
	private long sdid;
	private long trid;
	private String content;
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static SuperChatMessage getInstance(Map<String, String> message){
		return mapper.convertValue(message, SuperChatMessage.class);
	}
}
