package com.peace.elite.redisRepository.impl;

import static com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.longThat;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.peace.elite.redisRepository.GiftRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GiftRepository_ReceiveTest {

	@Autowired
	GiftRepository giftRepository;
	@Autowired
	StringRedisTemplate template;
	
	private ZSetOperations<String, String> zetOperations;
	private ValueOperations<String, String> valueOperations;
	long uid;
	long gid;
	long timeStamp = 1400000;
	String valueUserName = "peter";
	@Before
	public void resetRedisKeysForTest(){
		
		zetOperations = template.opsForZSet();
		valueOperations = template.opsForValue();
		
		uid=100l;
		gid=196;
		template.delete(USER(uid));
		template.delete(USERS_GIFT_RANK_TOTAL);
		template.delete(USERS_GIFT_RANK(gid));
		template.delete(COUNT_GIFT(gid));
		template.delete(GIVING(uid, gid));
		template.delete(GIVINGS_TIME_LINE);
		HashOperations<String, String, String> operations = template.opsForHash();
		operations.put(USER(uid), "name", "placeHolder");
		operations.put(USER(uid), "gift-sum", "0");
	}
	@Test
	public void receiveSetUserName(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		
		String updatedUserName=giftRepository.getUserName(USER(uid));
		assertTrue(updatedUserName, "peter".equals(updatedUserName));
	}
	
	@Test
	public void receiveDoNothingOnNullOrEmptyUserName(){
		String nullUserName = null;
		
		giftRepository.receive(uid, gid, timeStamp, nullUserName);
		String updatedUserName=giftRepository.getUserName(USER(uid));
		assertTrue(updatedUserName, "placeHolder".equals(updatedUserName));
	}
	
	@Test
	public void receivedGiftPriceSumsUp(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		assertTrue(giftRepository.getUserGiftSum(USER(uid)),"15000".equals(giftRepository.getUserGiftSum(USER(uid))));
	}
	
	@Test
	public void receiveAlsoUpdatesUserGiftRankTotal(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		double score = zetOperations.score(USERS_GIFT_RANK_TOTAL, USER(uid));
		assertTrue("giftPrice=10000;score:"+score, new Double(score).longValue()==10000l);
	}
	
	@Test
	public void receiveAlsoUpdatesUserGiftRankGid(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		double giftCount = zetOperations.score(USERS_GIFT_RANK(gid), USER(uid));
		assertTrue(giftCount+"",new Double(giftCount).longValue()==3);
	}
	
	@Test 
	public void receiveAlsoUpdateCountGiftGid(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		String giftCount = valueOperations.get(COUNT_GIFT(gid));
		assertTrue("giftCount="+giftCount, "3".equals(giftCount));
	}
	
	
	@Test
	public void receiveAllowSameGivingAtSameTime(){
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		giftRepository.receive(uid, gid, timeStamp, valueUserName);
		long counter = zetOperations.zCard(GIVINGS_TIME_LINE);
		assertTrue(zetOperations.reverseRange(GIVINGS_TIME_LINE, 0l, -1l)+"", counter==3l);
	}
	
}

