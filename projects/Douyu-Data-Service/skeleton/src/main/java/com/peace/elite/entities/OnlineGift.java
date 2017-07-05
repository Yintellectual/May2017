package com.peace.elite.entities;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineGift {

		private long id; 
		private String type;
		private long rid;
		private long uid;
		private long gid;
		private long sil;
		private long lf;
		private String ct;
		private String nn;
		
		public static ObjectMapper mapper = new ObjectMapper();
		public static OnlineGift getInstance(Map<String, String> message){
			return mapper.convertValue(message, OnlineGift.class);
		}
		
}
