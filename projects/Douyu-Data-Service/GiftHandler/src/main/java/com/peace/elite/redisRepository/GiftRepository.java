package com.peace.elite.redisRepository;

import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

public interface GiftRepository {
	//1. 
	//void receive(String keyUser, String keyGift, long timeStamp, String userName);
	void receive(long uid, long gid, long timeStamp, String valueUserName);
	//1.1
	//void userNameUpdate(String keyUser, String valueUserName);
	//void userNameUpdate(long uid, String userName);
	//1.2 atomically update the gift sums both in the user profile and the gift-rank:Total
	//void userGiftSumUpdate(String keyUser, long valueGiftPrice);
	//void userGiftSumUpdate(long uid, long giftPrice);
	//1.3
	//void userGiftRankSumUpdate(long valueGiftPrice, String keyUser);
	//void userGiftRankSumUpdate(long giftPrice, long uid);
	//1.4
	//void userGiftRankGidUpdate(long gid, String keyUser);
	//void userGiftRankGidUpdate(long gid, long uid);
	//1.5
	//void countGift(String keyCountGift);
	//void countGift(long gid);
	//1.6, store givings in string
	String getCountGiving(String keyGiving);
	String getCountGiving(long uid, long gid);
	//1.7 also count giving
	//void givingTimeLineUpdate(long timeStamp, String keyGiving);
	//void givingTimeLineUpdate(long timeStamp, long gid, long uid);
	
	//2. return list of uids, keyGift = [user:gift-rank:{gid}] [user:gift-rank:sum]

	//2.1 zrange
	Set<TypedTuple<String>> userGiftRankRankOriented(String keyGift, long startRank, long endRank);
	//2.2 zrangebyscore
	Set<TypedTuple<String>> userGiftRankScoreOriented(String keyGift, long startScore, long endScore);
	//2.3 search by the gift rank, because most interesting names are in high rank
	String getKeyUserByName(String name);
	String getUserName(String keyUser);
	String getUserGiftSum(String keyUser);
	String getUserGiftSumByName(String name);
	String getUserGiftSum(long uid);
	//3. return list of givings-keys
	Set<TypedTuple<String>> givingsTimeOriented(long start, long end);
	//4.
	String getCountGift(String keyCountGift);
	String getCountGift(long gid);
	
	//5.
	long getGiftPrice(long gid);
	String getGiftName(long gid);
	
	
}
