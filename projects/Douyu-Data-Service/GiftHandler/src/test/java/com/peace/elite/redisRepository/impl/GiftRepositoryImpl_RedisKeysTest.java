package com.peace.elite.redisRepository.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GiftRepositoryImpl_RedisKeysTest {
	
	@Autowired
	GiftRepositoryRedisImpl giftRepository;
	
	@Test
	public void test() {
		assertTrue(giftRepository!=null);
	}

	@Test 
	public void keyTest(){
		String generatedKey = key("dimension1", "dimension2", 100l+"");
		assertTrue((GLOBAL_PREFIX+"dimension1:dimension2:100").equals(generatedKey));
	}
		
	@Test
	public void keyTestUser(){
		String keyUser = USER(100l);
		assertTrue(keyUser ,"douyu-db:user:100".equals(keyUser));
	}
	
	@Test
	public void keyTestGift(){
		String key = GIFT(100l);
		assertTrue(key,"douyu-db:gift:100".equals(key));
	}
	@Test
	public void keyTestGiving(){
		String key = GIVING(10, 100);
		assertTrue(key,"douyu-db:giving:10:100".equals(key));
	}
	@Test
	public void keyTestGivingsTimeLing(){
		assertTrue(GIVINGS_TIME_LINE,"douyu-db:givings:time-line".equals(GIVINGS_TIME_LINE));
	}
	@Test
	public void keyTestUsersGiftRankTotal(){
		assertTrue(USERS_GIFT_RANK_TOTAL,"douyu-db:users:gift-rank:total".equals(USERS_GIFT_RANK_TOTAL));
	}
	@Test
	public void keyTestUsersGiftRank(){
		assertTrue(USERS_GIFT_RANK(100l), "douyu-db:users:gift-rank:100".equals(USERS_GIFT_RANK(100l)));
	}
	@Test
	public void keyTestCountGift(){
		assertTrue(COUNT_GIFT(100l),"douyu-db:count:gift:100".equals(COUNT_GIFT(100l)));
	}
	@Test
	public void keyTestGlobalGifts(){
		assertTrue(GLOBAL_GIFTS,"douyu-db:global:gifts".equals(GLOBAL_GIFTS));
	}
	@Test
	public void keyTestGlobalUsers(){
		assertTrue(GLOBAL_USERS,"douyu-db:global:users".equals(GLOBAL_USERS));
	}
	@Test
	public void keyTestCountUsers(){
		assertTrue(COUNT_USERS,"douyu-db:count:users".equals(COUNT_USERS));
	}
}
