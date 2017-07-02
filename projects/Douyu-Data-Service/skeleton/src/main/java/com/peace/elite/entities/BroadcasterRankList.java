package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;




@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BroadcasterRankList{

	private long id;
	private String type;
	private long rid;
	private long ts;
	private long gid;
	private String list_all;
	private String list;
	private String list_day;

	public static ObjectMapper mapper = new ObjectMapper();
	public static BroadcasterRankList getInstance(Map<String, String> message){
		return mapper.convertValue(message, BroadcasterRankList.class);
	}
}
