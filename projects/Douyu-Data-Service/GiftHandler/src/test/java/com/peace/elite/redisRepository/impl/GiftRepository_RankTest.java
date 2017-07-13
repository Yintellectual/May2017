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

import com.peace.elite.redisRepository.GiftRepository;

import lombok.Data;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GiftRepository_RankTest {

	@Autowired
	GiftRepository giftRepository;
	@Autowired
	StringRedisTemplate template;

	@Data
	public class TestGiving {

		public TestGiving() {

		}

		public TestGiving(long uid, long gid, long timeStamp, String valueUserName) {
			this.uid = uid;
			this.gid = gid;
			this.timeStamp = timeStamp;
			this.valueUserName = valueUserName;
		}

		private long uid;
		private long gid;
		private long timeStamp;
		private String valueUserName;

		public void receive() {
			giftRepository.receive(uid, gid, timeStamp, valueUserName);
		}
	}

	private ZSetOperations<String, String> zetOperations;
	private ValueOperations<String, String> valueOperations;
	private HashOperations<String, String, String> hashOperations;
	private LinkedList<TestGiving> testData;

	@Before
	public void init() {

		zetOperations = template.opsForZSet();
		valueOperations = template.opsForValue();
		hashOperations = template.opsForHash();
		testData = new LinkedList<>();

		template.keys("douyu-db-test:*").stream().forEach(key -> {
			template.delete(key);
		});
		Random random = new Random();
		for (int i = 0; i < 200; i++) {
			long uid = 1000 + random.nextInt(12);
			long gid = 195 + random.nextInt(2);
			long timeStamp = new Date().getTime() / 1000;
			String valueUserName = "User" + uid;
			TestGiving giving = new TestGiving(uid, gid, timeStamp, valueUserName);
			giving.receive();
			testData.add(giving);
		}
	}

	@After
	public void After() {
	}

	@Test
	public void userGiftRankRankOrientedFindScoresEqualsToGiftSum() {
		for (TypedTuple<String> userWithScore : giftRepository.userGiftRankRankOriented(USERS_GIFT_RANK_TOTAL, 0, -1)) {
			String keyUser = userWithScore.getValue();
			double score = userWithScore.getScore();
			String userGiftSum =  giftRepository.getUserGiftSum(keyUser);
			System.out.println("User:" + giftRepository.getUserName(keyUser)

					+ ",  Score in RankList: " + score + ",  Gift-Sum: " + userGiftSum);
			assertTrue(score == Double.parseDouble(userGiftSum));
		}

	}

	@Test
	public void userGiftRankRankOrientedReturnInDesiredOrder() {
		double previousScore = Double.MAX_VALUE;
		for (TypedTuple<String> userWithScore : giftRepository.userGiftRankRankOriented(USERS_GIFT_RANK_TOTAL, 0, -1)) {
			double score = userWithScore.getScore();
			assertTrue("previousScore: "+previousScore+"    score: "+score,previousScore >= score);
			previousScore = score;
		}
	}
	
	@Test 
	public void userGiftRankScoreOrientedReturnsBigFirst(){
		double previousScore = Double.MAX_VALUE;
		for (TypedTuple<String> userWithScore : giftRepository.userGiftRankScoreOriented(USERS_GIFT_RANK_TOTAL, 80000l, 50000l)) {
			double score = userWithScore.getScore();
			assertTrue(previousScore >= score);
			previousScore = score;
		}
	}
	@Test 
	public void userGiftRankScoreOrientedReturnsWithInRange(){
		for (TypedTuple<String> userWithScore : giftRepository.userGiftRankScoreOriented(USERS_GIFT_RANK_TOTAL,80000l ,50000l )) {
			double score = userWithScore.getScore();
			assertTrue(score <= 80000l && score >= 50000l);
		}
	}
	
	@Test 
	public void getKeyUserByNameDontWorkTooSlow(){
		template.keys("douyu-db-test:*").stream().forEach(key -> {
			template.delete(key);
		});
		Random random = new Random();
		long start = new Date().getTime();
		//takes about 13s 
		//IntStream.range(0, 50000).parallel().forEach(i->{
		new TestGiving(1010l, 196, new Date().getTime()/1000, "User"+1010).receive();
		IntStream.range(0, 5).parallel().forEach(i->{
			long uid = 1000 + random.nextInt(10000);
			long gid = 195 + random.nextInt(2);
			long timeStamp = new Date().getTime() / 1000;
			String valueUserName = "User" + uid;
			TestGiving giving = new TestGiving(uid, gid, timeStamp, valueUserName);
			giving.receive();
			testData.add(giving);
		});
		long consumedTime = new Date().getTime() - start;
		System.out.println("\n\n\n\n~~~~~~~~~~~~"+consumedTime);
		start = new Date().getTime();
		assertTrue((USER(1010l).equals(giftRepository.getKeyUserByName("User"+1010))));
		consumedTime = new Date().getTime() - start;
		System.out.println("\n\n\n\n~~2~~~2~~2~~~~~"+consumedTime);
	}
	
	@Test
	public void givingsTimeOrientedReturnGivingsInReverseTimeOrder(){
		template.keys("douyu-db-test:*").stream().forEach(key -> {
			template.delete(key);
		});
		Random random = new Random();
		long start = new Date().getTime();
		//takes about 13s 
		//IntStream.range(0, 50000).parallel().forEach(i->{
		IntStream.range(0, 5).parallel().forEach(i->{
			long uid = 1000 + random.nextInt(10000);
			long gid = 195 + random.nextInt(2);
			long timeStamp = new Date().getTime() / 1000;
			String valueUserName = "User" + uid;
			TestGiving giving = new TestGiving(uid, gid, timeStamp, valueUserName);
			giving.receive();
			testData.add(giving);
		});
		long criterionTimestamp = new Date().getTime() /1000;
		Set<TypedTuple<String>> givings = giftRepository.givingsTimeOriented(criterionTimestamp, criterionTimestamp-3);
		long previousTimestamp = Long.MAX_VALUE;
		for (TypedTuple<String> givingWithTimestamp : givings) {
			long timestamp = new Double(givingWithTimestamp.getScore()).longValue();
			assertTrue(previousTimestamp >= timestamp);
			assertTrue(timestamp >= criterionTimestamp-3 && timestamp <= criterionTimestamp);
			previousTimestamp = timestamp;
		}
	}
}
