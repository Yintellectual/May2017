package com.peace.elite.redisMQ;


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
	
	private String keyGenerate(DouyuMessageType douyuMessageType){
		return "mq:"+douyuMessageType.name();
	}
	
	/*
	 * left push, right block pop
	 */
	public void messageQueue(DouyuMessageType douyuMessageType, String message){
	
			System.out.println(douyuMessageType);
		
		listOps.leftPush(keyGenerate(douyuMessageType), message);
	}
}
