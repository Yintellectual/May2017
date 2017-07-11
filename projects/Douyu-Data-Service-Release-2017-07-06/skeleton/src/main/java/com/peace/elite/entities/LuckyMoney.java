package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;




@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LuckyMoney{

	private long id;
	private String type;
	private long rid;
	private long gid;
	private long sl;
	private long sid;
	private long did;
	private String snk;
	private String dnk;
	private long rpt;
	

	public static ObjectMapper mapper = new ObjectMapper();
	public static LuckyMoney getInstance(Map<String, String> message){
		return mapper.convertValue(message, LuckyMoney.class);
	}
}
