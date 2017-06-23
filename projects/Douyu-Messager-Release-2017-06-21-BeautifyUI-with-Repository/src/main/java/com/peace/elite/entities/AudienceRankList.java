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
public class AudienceRankList{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String type;
	private long rid;
	private long gid;
	private long uid;
	private long drid;
	private long rt;
	private long bt;
	private long sz;
	private String nk;
	private long rkt;
	private long rn;

	public static ObjectMapper mapper = new ObjectMapper();
	public static AudienceRankList getInstance(Map<String, String> message){
		return mapper.convertValue(message, AudienceRankList.class);
	}
}
