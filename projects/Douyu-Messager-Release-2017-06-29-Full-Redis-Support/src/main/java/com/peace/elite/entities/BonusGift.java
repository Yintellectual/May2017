package com.peace.elite.entities;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class BonusGift{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String type;
	private long rid;
	private long gid;
	private long level;
	private long cnt;
	private long hits;
	private long lev;
	private String sui;
	
	private long uid;
	
	public void setSui(String sui){
		this.sui = sui;
		Pattern p = Pattern.compile("id@=(\\d*)");
		Matcher m = p.matcher("sui");
		while(m.find()){
			this.uid= Long.parseLong(m.group(1));
		}
	}
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static BonusGift getInstance(Map<String, String> message){
		return mapper.convertValue(message, BonusGift.class);
	}
}

