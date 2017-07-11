package com.peace.elite.redisRepository.impl;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.peace.elite.redisRepository.GiftRepository;
@Repository
public class GiftRepositoryRedisImpl implements GiftRepository {

	public final static String GLOBAL_PREFIX = "douyu-db:"; 
	public final static String DELIMITER = ":";
	public static String GIVINGS_TIME_LINE;
	public static String USERS_GIFT_RANK_TOTAL;
	public static String GLOBAL_USERS;
	public static String GLOBAL_GIFTS;
	public static String COUNT_USERS;
	
	static {
		GIVINGS_TIME_LINE= key("givings","time-line");
		USERS_GIFT_RANK_TOTAL=key("users", "gift-rank", "total");
		GLOBAL_USERS = key("global", "users");
		GLOBAL_GIFTS = key("global", "gifts");
		COUNT_USERS = key("count", "users");
	}
	
	public static String key(String ... args){
		return GLOBAL_PREFIX+Arrays.stream(args).collect(Collectors.joining(DELIMITER));
	}
	public static String USER(long uid){
		return key("user", ""+uid); 
	}
	public static String GIFT(long gid){
		return key("gift", ""+gid);
	}
	public static String GIVING(long uid, long gid){
		return key("giving", ""+uid, ""+gid);
	}
	public static String USERS_GIFT_RANK(long gid){
		return key("users", "gift-rank", ""+gid);
	}
	public static String COUNT_GIFT(long gid){
		return key("count","gift",""+gid);
	}
	
	
	@Override
	public void receive(String keyUser, String keyGift, String timeStamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receive(long uid, long gid, long timeStamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userNameUpdate(String keyUser, String valueUserName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userNameUpdate(long uid, String userName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftSumUpdate(String keyUser, String valueGiftPrice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftSumUpdate(long uid, long giftPrice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftRankSumUpdate(String valueGiftPrice, String uid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftRankSumUpdate(long giftPrice, long uid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftRankGidUpdate(String gid, String uid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userGiftRankGidUpdate(long gid, long uid) {
		// TODO Auto-generated method stub

	}
	@Override
	public void totalGift(String giftPrice){
		
	}
	
	@Override
	public void countGift(String keyCountGift) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countGift(long gid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countGiving(String KeyGiving) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countGiving(long uid, long gid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void givingTimeLineUpdate(String timeStamp, String gid, String uid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void givingTimeLineUpdate(long timeStamp, long gid, long uid) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> userGiftRankRankOriented(String keyGiftRank, String startRank, String endRank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> userGiftRankScoreOriented(String keyGiftRank, String startScore, String endScore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUidByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName(String keyUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserGiftSum(String keyUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserGiftSumByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserGiftSum(long uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> givingTimeOriented(String timeStamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCountGift(String keyCountGift) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCountGift(long gid) {
		// TODO Auto-generated method stub
		return null;
	}

}
