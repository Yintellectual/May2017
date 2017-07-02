package com.peace.elite.entities;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String type;
	private long gid;
	private long rid;
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
