package com.peace.elite.redisRepository.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import org.springframework.stereotype.Repository;


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
	
	double smallMoney = 0.0;
	@Override
	public void moneyAdd(SmallGift smallGift){
		try{
			hashOps.put(appendKeyById(audience_uid, smallGift.getUid()),"name",smallGift.getNn());
		}catch(Exception e){
			System.out.println(smallGift);
			e.printStackTrace();
		}
		long uid = smallGift.getUid();
		long amount = 0;
		switch((int)smallGift.getGfid()){
			//办卡
			case 924:
				amount = 6;
				break;
			//猫耳 0.2
			case 529:
				 smallMoney+=0.2;
				 break;
			//荧光棒
			case 824:
				smallMoney+=0.1;
				break;
			
			//弱鸡
			case 193:
				smallMoney+=0.2;
				break;
			//怂
			case 713:
				smallMoney+=0.1;
				break;
			//赞
			case 192:
				smallMoney+=0.1;
				break;
			//呵呵
			case 519:
				smallMoney+=0.1;
				break;
			
		    //稳 
			case 520:
				smallMoney+=0.1;
				break;
			//双马尾 0.1
			case 918:
				 smallMoney+=0.1;
				 break;
			case 195:
				amount = 100;
				break;
			case 196:
				amount = 500;
				break;
			default: 
				break;
		}
		if(amount!=0){
			moneyAdd(uid, amount);
		}else {
			if(smallMoney>100.0){
				moneyAdd(0L, 100L);
				smallMoney -= 100;
			}
		}
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
