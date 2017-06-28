package com.peace.elite.redisRepository.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.peace.elite.entities.BigGift;
import com.peace.elite.entities.ChatMessage;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.redisRepository.AudienceRedisRepository;
@Repository
public class AudienceRedisRepositoryImpl implements AudienceRedisRepository {

	/*
	 * hash audience:uid chat 11 money 550 name user1
	 * 
	 * zet audience:chat:uid aaaa timestamp bbbb timestamp ccc timestamp 
	 * 
	 * zet audience:money:uid 500 timestamp 50 timestamp
	 * 
	 * zet audiences:chat user1 235 user2 221
	 * 
	 * zet audiences:money user1 20050 user2 15000
	 * 
	 */
	private RedisTemplate<String, String> redisTemplate;
	
	private static final String audience_uid = "audience";
	private static final String audience_chat_uid = "audience:chat";
	private static final String audience_money_uid = "audience:money";
	private static final String audiences_chat = "audiences:chat";
	private static final String audiences_money = "audiences:money";
	
	private HashOperations<String, String, String> hashOps;
	private ZSetOperations<String, String> zetOps;
	
	@Autowired
	public AudienceRedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	@PostConstruct
	private void init(){
		hashOps = redisTemplate.opsForHash();
		zetOps = redisTemplate.opsForZSet();
	}
	
	private String appendKeyById(String key, long id){
		return key+":"+id;
	}
	private String appendKeyById(String key, String id){
		return key+":"+id;
	}
	private String appendValueByTimeStamp(String value, long timeStamp){
		return value+":TIMESTAMP"+timeStamp;
	}
	
	@Override
	public void chatAdd(ChatMessage chatMessage) {
		
		long timeStamp = new Date().getTime()/1000;
		long uid = chatMessage.getUid();
		String name = chatMessage.getNn();
		String word = chatMessage.getTxt();
		//if user in hash, incr chat 
		hashOps.increment(appendKeyById(audience_uid, uid), "chat", 1);
		//zet audience:chat:uid add chat score timestamp
		zetOps.add(appendKeyById(audience_chat_uid, uid), appendValueByTimeStamp(word, timeStamp), timeStamp);
		//zet audiences:chat user incr score 
		zetOps.incrementScore(audiences_chat, ""+uid, 1);
	}
	
	
	@Override
	public void moneyAdd(SmallGift smallGift){
		long timeStamp = new Date().getTime()/1000;
		long uid = smallGift.getUid();
		
		String name = smallGift.getNn();
		long amount = 0;
		switch((int)smallGift.getGfid()){
			case 195:
				amount = 100;
				break;
			case 196:
				amount = 500;
				break;
			default: 
				break;
		}
		hashOps.increment(appendKeyById(audience_uid, uid), "money", amount);
		zetOps.add(appendKeyById(audience_money_uid, uid), appendValueByTimeStamp(""+amount, timeStamp), timeStamp);
		zetOps.incrementScore(audiences_money, ""+uid, amount);
	}
	
	

	
}
