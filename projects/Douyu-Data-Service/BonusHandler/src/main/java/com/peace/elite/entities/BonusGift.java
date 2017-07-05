package com.peace.elite.entities;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BonusGift{

	private long id;
	private String type;
	private long rid;
	private long gid;
	private long level;
	private long cnt;
	private long hits;
	private long lev;
	private String sui;
	private String nn;
	
	private long uid;
	
	public static long retrieveUid(String sui){
		Pattern p = Pattern.compile("id@=(.*?)\\/");
		Matcher m = p.matcher(sui);
		long result = 0;
		if(m.find()){
			result = Long.parseLong(m.group(1));
		}
		
		return result;
	} 

	public static String retrieveNn(String sui){
		String result = "";
		Pattern p2 = Pattern.compile("nick@=(.*?)\\/");
		Matcher m2 = p2.matcher(sui);
		if(m2.find()){
			result = m2.group(1);
		}
		
		return result;
	} 
	
	public void setSui(String sui){
		this.sui = sui;
		this.uid = retrieveUid(sui);
		this.nn = retrieveNn(sui);
		
	}
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static BonusGift getInstance(Map<String, String> message){
		return mapper.convertValue(message, BonusGift.class);
	}
}

