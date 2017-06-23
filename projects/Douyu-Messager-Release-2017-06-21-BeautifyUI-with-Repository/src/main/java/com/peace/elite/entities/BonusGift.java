package com.peace.elite.entities;

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
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static BonusGift getInstance(Map<String, String> message){
		return mapper.convertValue(message, BonusGift.class);
	}
}

