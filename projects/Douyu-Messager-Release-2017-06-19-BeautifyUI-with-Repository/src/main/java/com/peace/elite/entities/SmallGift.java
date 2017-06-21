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
public class SmallGift{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String type;
	private long rid;
	private long gif;
	private long gfid;
	private long gs;
	private long uid;
	private String nn;
	private long str;
	private long level;
	private long dw;
	private long gfcnt;
	private long hits;
	private long dlv;
	private long dc;
	private long bdl;
	private long rg;
	private long pg;
	private long rpid;
	private long slt;
	private long elt;
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static SmallGift getInstance(Map<String, String> message){
		return mapper.convertValue(message, SmallGift.class);
	}
}
