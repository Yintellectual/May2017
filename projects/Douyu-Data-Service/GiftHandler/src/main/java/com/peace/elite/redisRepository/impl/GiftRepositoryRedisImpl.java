package com.peace.elite.redisRepository.impl;

import java.util.Set;
import java.util.stream.Collectors;

import static com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl.USERS_GIFT_RANK_TOTAL;


import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Event;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.Synchronized;

@Repository
public class GiftRepositoryRedisImpl implements GiftRepository {

	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	
	public final static String GLOBAL_PREFIX = "douyu-db:";
	public final static String DELIMITER = ":";
	public static String GIVINGS_TIME_LINE;
	public static String USERS_GIFT_RANK_TOTAL;
	public static String GLOBAL_USERS;
	public static String GLOBAL_GIFTS;
	public static String COUNT_USERS;

	static {
		GIVINGS_TIME_LINE = key("givings", "time-line");
		USERS_GIFT_RANK_TOTAL = key("users", "gift-rank", "total");
		GLOBAL_USERS = key("global", "users");
		GLOBAL_GIFTS = key("global", "gifts");
		COUNT_USERS = key("count", "users");
	}

	public static String key(String... args) {
		return GLOBAL_PREFIX + Arrays.stream(args).collect(Collectors.joining(DELIMITER));
	}

	public static String USER(long uid) {
		return key("user", "" + uid);
	}

	public static String GIFT(long gid) {
		return key("gift", "" + gid);
	}

	public static String GIVING(long uid, long gid) {
		return key("giving", "" + uid, "" + gid);
	}

	public static String USERS_GIFT_RANK(long gid) {
		return key("users", "gift-rank", "" + gid);
	}

	public static String COUNT_GIFT(long gid) {
		return key("count", "gift", "" + gid);
	}

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	private HashOperations<String, String, String> hashOperations;
	private ZSetOperations<String, String> zetOperations;
	private ValueOperations<String, String> valueOperations;

	@PostConstruct
	private void init() {
		hashOperations = stringRedisTemplate.opsForHash();
		zetOperations = stringRedisTemplate.opsForZSet();
		valueOperations = stringRedisTemplate.opsForValue();
		receivingEventFactory.register(this);
	}

	@Override
	@Synchronized
	public void receive(long uid, long gid, long timeStamp, String valueUserName) {
		// TODO Auto-generated method stub
		boolean updateName = false;
		if (valueUserName == null || valueUserName.isEmpty()) {
			updateName = false;
		} else {
			updateName = true;
		}
		String keyUser = USER(uid);
		String keyGiving = GIVING(uid, gid);
		String countGiving = getCountGiving(keyGiving);
		long valueGiftPrice = getGiftPrice(gid);
		stringRedisTemplate.setEnableTransactionSupport(true);
		stringRedisTemplate.multi();

		if (updateName) {
			hashOperations.put(keyUser, "name", valueUserName);
		}
		hashOperations.increment(keyUser, "gift-sum", valueGiftPrice);
		zetOperations.incrementScore(USERS_GIFT_RANK_TOTAL, keyUser, (double) valueGiftPrice);

		valueOperations.increment(keyGiving, 1l);
		zetOperations.incrementScore(GIVINGS_TIME_LINE, keyGiving + ":" + countGiving, timeStamp);
		zetOperations.incrementScore(USERS_GIFT_RANK(gid), keyUser, (double) 1);
		valueOperations.increment(COUNT_GIFT(gid), 1l);

		stringRedisTemplate.exec();
		
//		if(zetOperations.score(GIVINGS_TIME_LINE, keyGiving + ":" + countGiving)>1500001462000l*1.01){
//			System.out.println(keyGiving + ":" + countGiving+":"+timeStamp);
//			throw new RuntimeException();
//		}
		
		
	}

	@Override
	public String getCountGiving(String keyGiving) {
		return valueOperations.get(keyGiving);

	}

	@Override
	public String getCountGiving(long uid, long gid) {
		return getCountGiving(GIVING(uid, gid));
	}

	@Override
	public Set<TypedTuple<String>> userGiftRankRankOriented(String keyGiftRank, long startRank, long endRank) {
		// TODO Auto-generated method stub
		return zetOperations.reverseRangeWithScores(keyGiftRank, startRank, endRank);
	}

	@Override
	public Set<TypedTuple<String>> userGiftRankScoreOriented(String keyGiftRank, long startScore, long endScore) {
		// TODO Auto-generated method stub
		return zetOperations.reverseRangeByScoreWithScores(keyGiftRank, (double) startScore, (double) endScore);
	}

	
	/*
	 * search by the ranklist 
	 * return String keyUser
	 */
	@Override
	public String getKeyUserByName(String name) {
		for (TypedTuple<String> userWithScore : userGiftRankRankOriented(USERS_GIFT_RANK_TOTAL, 0l, -1)) {
			String keyUser = userWithScore.getValue();
			if(name.equals(getUserName(keyUser))){
				return keyUser;
			}
		}
		return null;
	}

	@Override
	public String getUserName(String keyUser) {
		// TODO Auto-generated method stub
		return hashOperations.get(keyUser, "name");
	}

	@Override
	public String getUserGiftSum(String keyUser) {
		// TODO Auto-generated method stub
		return hashOperations.get(keyUser, "gift-sum");
	}

	@Override
	public String getUserGiftSumByName(String name) {
		// TODO Auto-generated method stub
		return getUserGiftSum(getKeyUserByName(name));
	}

	@Override
	public String getUserGiftSum(long uid) {
		// TODO Auto-generated method stub
		return getUserGiftSum(USER(uid));
	}

	@Override
	public Set<TypedTuple<String>> givingsTimeOriented(long start, long end) {
		// TODO Auto-generated method stub
		return zetOperations.reverseRangeByScoreWithScores(GIVINGS_TIME_LINE, (double)start, (double)end);
	}

	@Override
	public String getCountGift(String keyCountGift) {
		// TODO Auto-generated method stub
		return valueOperations.get(keyCountGift);
	}

	@Override
	public String getCountGift(long gid) {
		// TODO Auto-generated method stub
		return getCountGift(COUNT_GIFT(gid));
	}

	public long getGiftPrice(long gid){
		long amount_in_cents;
		switch((int)gid){
		//办卡
		case 924:
			amount_in_cents = 60;
			break;
		//猫耳 0.2
		case 529:
			amount_in_cents = 2;
			 break;
		//荧光棒
		case 824:
			amount_in_cents = 1;
			break;
		
		//弱鸡
		case 193:
			amount_in_cents = 2;
			break;
		//怂
		case 713:
			amount_in_cents = 1;
			break;
		//赞
		case 192:
			amount_in_cents = 1;
			break;
		//呵呵
		case 519:
			amount_in_cents = 1;
			break;
		
	    //稳 
		case 520:
			amount_in_cents = 1;
			break;
		//双马尾 0.1
		case 918:
			amount_in_cents = 1;
			 break;
		case 195:
			amount_in_cents = 1000;
			break;
		case 196:
			amount_in_cents = 5000;
			break;
		default: 
			amount_in_cents =0;
			break;
		}
		return amount_in_cents;
	}

	public String getGiftName(long gid) {
		String name;
		switch((int)gid){
		//办卡
		case 924:
			name = "办卡";
			break;
		//猫耳 0.2
		case 529:
			name = "猫耳";
			 break;
		//荧光棒
		case 824:
			name = "荧光棒";
			break;
		
		//弱鸡
		case 193:
			name = "弱鸡";
			break;
		//怂
		case 713:
			name = "怂";
			break;
		//赞
		case 192:
			name = "赞";
			break;
		//呵呵
		case 519:
			name = "呵呵";
			break;
		
	    //稳 
		case 520:
			name = "稳";
			break;
		//双马尾 0.1
		case 918:
			name = "双马尾";
			break;
		case 195:
			name = "飞机";
			break;
		case 196:
			name = "火箭";
			break;
		default: 
			name = "礼物编号"+gid;
			break;
		}
		return name;		
	}

	@Override
	public String getUserName(long uid) {
		// TODO Auto-generated method stub
		return getUserName(USER(uid));
	}

	@Override
	public void handle(Event<SmallGift> e) {
		// TODO Auto-generated method stub
		long uid = e.getData().getUid();
		long gid = e.getData().getGfid();
		long timeStamp = new Date().getTime();
		String valueUserName = e.getData().getNn();
		this.receive(uid, gid, timeStamp, valueUserName);
	}
}
