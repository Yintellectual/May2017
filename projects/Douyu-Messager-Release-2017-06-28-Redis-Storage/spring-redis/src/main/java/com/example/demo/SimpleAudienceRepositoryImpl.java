package com.example.demo;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleAudienceRepositoryImpl implements AudienceRepository {

	private static final String KEY = "Audience";
	private RedisTemplate<String, String> redisTemplate;
	private HashOperations<String, String, String> hashOps;
	private ZSetOperations<String, String> zortOps;
	@Autowired
	public SimpleAudienceRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init(){
		hashOps = redisTemplate.opsForHash();
		zortOps = redisTemplate.opsForZSet();
	}
	
	@Override
	public void audienceAdd(Audience audience) {
		System.out.println("\n"+audience.getMoney()+"\n\n"+
		hashOps.putIfAbsent(KEY+":"+audience.getUid(),"money" , ""+audience.getMoney())
		+"");
		System.out.println("\n\n\n"+
		hashOps.putIfAbsent(KEY+":"+audience.getUid(),"chat" , ""+audience.getChat())
		+"");
		System.out.println("b");
	
		zortOps.add(KEY+":"+audience.getUid()+":"+"money", "2", (new Date().getTime())/1000);
		zortOps.add(KEY+":"+audience.getUid()+":"+"chat", "hey", (new Date().getTime())/1000);
	}

	@Override
	public void moneyIncrBy(long uid, long amont) {

		hashOps.increment(KEY+":"+uid, "money", amont);
		System.out.println("c");
	}

	@Override
	public void chatIncr(long uid) {

		hashOps.increment(KEY+":"+uid, "chat", 1L);
		System.out.println(
		""+ hashOps.entries(KEY+":"+uid)		
		);
	}
	
	@Override 
	public void chatRecord(long uid, String chat){
		chatIncr(uid);
		zortOps.add(KEY+":"+uid+":"+"chat", chat, (new Date().getTime())/1000);
	}
	

}
