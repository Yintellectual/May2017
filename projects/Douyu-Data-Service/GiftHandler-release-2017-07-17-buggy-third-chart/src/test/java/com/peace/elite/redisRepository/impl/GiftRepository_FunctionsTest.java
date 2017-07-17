package com.peace.elite.redisRepository.impl;

import static com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl.*;
import static org.junit.Assert.assertTrue;

import java.security.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.swing.JComboBox.KeySelectionManager;

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
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.junit4.SpringRunner;

import com.peace.elite.entities.Giving;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.Data;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GiftRepository_FunctionsTest {

	@Autowired
	GiftRepository giftRepository;
	@Autowired
	StringRedisTemplate template;


	private ZSetOperations<String, String> zetOperations;
	private ValueOperations<String, String> valueOperations;
	private HashOperations<String, String, String> hashOperations;
	private LinkedList<Giving> testData;

	@Before
	public void init() {

		zetOperations = template.opsForZSet();
		valueOperations = template.opsForValue();
		hashOperations = template.opsForHash();
		testData = new LinkedList<>();

		template.keys("douyu-db:*").stream().forEach(key -> {
			template.delete(key);
		});
		Random random = new Random();
		for (int i = 0; i < 200; i++) {
			long uid = 1000 + random.nextInt(12);
			long gid = 195 + random.nextInt(2);
			long timeStamp = new Date().getTime() / 1000;
			String valueUserName = "User" + uid;
			Giving giving = new Giving(uid, gid, timeStamp, valueUserName);
			giftRepository.receive(uid, gid, timeStamp, valueUserName);
			testData.add(giving);
		}
	}

	@After
	public void After() {
	}

	@Test
	public void testSum(){
		testData = new LinkedList<>();
		template.keys("douyu-db:*").stream().forEach(key -> {
			template.delete(key);
		});
		Random random = new Random();
		for (int i = 0; i < 200; i++) {
			long uid = 1000;
			long gid = 195;
			long timeStamp = new Date().getTime() / 1000;
			String valueUserName = "User" + uid;
			Giving giving = new Giving(uid, gid, timeStamp, valueUserName);
			giftRepository.receive(uid, gid, timeStamp, valueUserName);
			testData.add(giving);
		}
		assertTrue("sumOfPrices is null!",GiftRepository.sumOfPrices!=null);
		
		//long sum = GiftRepository.sumOfPrices.apply(testData);
		//assertTrue(""+sum, sum == 200*1000);
	}
}
