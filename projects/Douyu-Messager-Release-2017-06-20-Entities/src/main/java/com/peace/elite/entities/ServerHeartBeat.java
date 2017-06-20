package com.peace.elite.entities;

import javax.persistence.Entity;


public class ServerHeartBeat{
	private DouyuMessageType type = DouyuMessageType.keeplive;
	private long tick; 
}
