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
public class BigGift{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String type;
	private long rid;
	private long gid;
	private String sn;
	private String dn;
	private String gn;
	private long gc;
	private long drid;
	private long gs;
	private long gb;
	private long es;
	private long gfid;
	private long eid;
	

	public static ObjectMapper mapper = new ObjectMapper();
	public static BigGift getInstance(Map<String, String> message){
		return mapper.convertValue(message, BigGift.class);
	}
}
