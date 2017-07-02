package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BroadcastStatus{

	private long id;
	private String type;
	private long rid;
	private long gid;
	private long ss;
	private long code;
	private long rt;
	private long notify;
	private long endtime;

	public static ObjectMapper mapper = new ObjectMapper();
	public static BroadcastStatus getInstance(Map<String, String> message){
		return mapper.convertValue(message, BroadcastStatus.class);
	}
}
