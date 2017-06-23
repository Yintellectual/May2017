package com.peace.elite.entities;

import java.rmi.server.UID;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineGift {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
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
