package com.example.demo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Audience implements Serializable {
	
	private String name;
	private long uid;
	private long chat;
	private long money;
	private long time;
}
