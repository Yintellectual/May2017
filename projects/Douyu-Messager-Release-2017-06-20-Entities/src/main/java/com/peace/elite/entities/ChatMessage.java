package com.peace.elite.entities;

import java.rmi.server.UID;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {
	private String type;
	private long gid;
	private long id;
	private long uid;
	private String nn;
	private String txt;
	private String cid;
	private long level;
	private long gt;
	private long col;
	private long ct;
	private long rg;
	private long pg;
	private long dlv;
	private long dc;
	private long bdlv;
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static ChatMessage getInstance(Map<String, String> message){
		return mapper.convertValue(message, ChatMessage.class);
	}
	
}
