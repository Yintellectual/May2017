package com.peace.elite.redisRepository.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.peace.elite.entities.BigGift;
import com.peace.elite.entities.BonusGift;
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
		hashOps.put(appendKeyById(audience_uid, uid),"name",name);
		hashOps.increment(appendKeyById(audience_uid, uid), "chat", 1);
		//zet audience:chat:uid add chat score timestamp
		zetOps.add(appendKeyById(audience_chat_uid, uid), appendValueByTimeStamp(word, timeStamp), timeStamp);
		//zet audiences:chat user incr score 
		zetOps.incrementScore(audiences_chat, ""+uid, 1);
	}
	
	
	@Override
	public void moneyAdd(SmallGift smallGift){
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
		moneyAdd(uid, amount);
	}
	@Override
	public void moneyAdd(BonusGift bonusGift){
		long uid = bonusGift.getUid();
		long lev = bonusGift.getLev();
		long amount = 0;
		switch((int)lev){
			case 1:
				amount = 15;
				break;
			case 2:
				amount = 30;
				break;
			case 3:
				amount = 50;
				break;
			default: 
				break;
		}
		moneyAdd(uid, amount);
	}
	private void moneyAdd(long uid, long amount){
		long timeStamp = new Date().getTime()/1000;
		hashOps.increment(appendKeyById(audience_uid, uid), "money", amount);
		zetOps.add(appendKeyById(audience_money_uid, uid), appendValueByTimeStamp(""+amount, timeStamp), timeStamp);
		zetOps.incrementScore(audiences_money, ""+uid, amount);
	}
	
	@Override
	public Map<String, String> audienceFetch(long uid){
		return hashOps.entries(appendKeyById(audience_uid, uid));
	}
	
	@Override
	public Set<String> moneyTop(int count){
		return zetOps.reverseRange(audiences_money, 0, count);
	}
	
	@Override
	public Set<String> chatTop(int count){
		return zetOps.reverseRange(audiences_chat, 0, count);
	}
	
	@Override
	public Set<String> audienceChatFetch(long uid){
		return zetOps.reverseRange(appendKeyById(audience_chat_uid, uid), 0, -1);
	}
	
	@Override
	public Set<String> audienceMoneyFetch(long uid){
		return zetOps.reverseRange(appendKeyById(audience_money_uid, uid), 0, -1);
	}
	
	
	/*
	 * painful O(nm) slow query
	 */
	@Override
	public long findUid(String name){
		Set<String> audienceKeys = redisTemplate.keys(appendKeyById(audience_uid,"*"));
		System.out.println("xxxccccccccxxxxxxxxxParsing\n\n"+audienceKeys);
		for(String key:audienceKeys){
			if(key.split(":").length == 2)
				if(name.equals(hashOps.get(key, "name"))){
					return retrieveUidFromKey(key); 
				}	
		}
		return -1L;
	}
	private long retrieveUidFromKey(String key){
		String[] split = key.split(":");
		long result = -1L;
		System.out.println("xxxxxxxxxxxxParsing");
		try{
			result = Long.parseLong(split[split.length-1]);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return result;
	}
}
