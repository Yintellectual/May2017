package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEnter{
	private long id;
	private String type;
	private long rid;
	private long gid;
	private long uid;
	private String nn;
	private long str;
	private long level;
	private long gt;
	private long rg;
	private long pg;
	private long dlv;
	private long dc;
	private long bdlv;
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static UserEnter getInstance(Map<String, String> message){
		return mapper.convertValue(message, UserEnter.class);
	}
}

