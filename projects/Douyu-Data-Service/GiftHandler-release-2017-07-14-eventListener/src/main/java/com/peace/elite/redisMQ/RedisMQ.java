package com.peace.elite.redisMQ;


import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.peace.elite.entities.DouyuMessageType;


/*
 * mq:{DouyuMessageType}:{index} 
 * 
 */
@Repository
public class RedisMQ {

	
	private RedisTemplate<String, String> redisTemplate;
	private ListOperations<String, String> listOps;
	
	
	@Autowired
	public RedisMQ(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	@PostConstruct
	private void init(){
		listOps = redisTemplate.opsForList();
	}
	
	/*
	 * mq:{douyuMessageType}
	 */
	private String keyGenerate(DouyuMessageType douyuMessageType){
		return "mq:"+douyuMessageType.name();
	}

	/*
	 * for display, mq:display:{douyuMessageType}
	 */
	private String keyGenerate(String str){
		return "mq:"+str;
	}
	/*
	 * left push, right block pop
	 */
	public String messagePop(DouyuMessageType douyuMessageType){
		return listOps.rightPop(keyGenerate(douyuMessageType), 0, TimeUnit.SECONDS);
	}

	/*
	 * left push, right block pop
	 */
	public void messageDisplay(String message){
		listOps.leftPush(keyGenerate("display"), message); 
	}
	
}
