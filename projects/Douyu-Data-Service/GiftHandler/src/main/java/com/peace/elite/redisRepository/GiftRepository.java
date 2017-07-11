package com.peace.elite.redisRepository;

import java.util.Set;

public interface GiftRepository {
	//1. 
	void receive(String keyUser, String keyGift, String timeStamp);
	void receive(long uid, long gid, long timeStamp);
	//1.1
	void userNameUpdate(String keyUser, String valueUserName);
	void userNameUpdate(long uid, String userName);
	//1.2
	void userGiftSumUpdate(String keyUser, String valueGiftPrice);
	void userGiftSumUpdate(long uid, long giftPrice);
	//1.3
	void userGiftRankSumUpdate(String valueGiftPrice, String uid);
	void userGiftRankSumUpdate(long giftPrice, long uid);
	//1.4
	void userGiftRankGidUpdate(String gid, String uid);
	void userGiftRankGidUpdate(long gid, long uid);
	//1.5
	void totalGift(String giftPrice);
	void countGift(String keyCountGift);
	void countGift(long gid);
	//1.6, store givings in hash, contains uid, gid and count
	void countGiving(String KeyGiving);
	void countGiving(long uid, long gid);
	//1.7
	void givingTimeLineUpdate(String timeStamp, String gid, String uid);
	void givingTimeLineUpdate(long timeStamp, long gid, long uid);
	
	//2. return list of uids, keyGift = [user:gift-rank:{gid}] [user:gift-rank:sum]

	//2.1 zrange
	Set<String> userGiftRankRankOriented(String keyGift, String startRank, String endRank);
	//2.2 zrangebyscore
	Set<String> userGiftRankScoreOriented(String keyGift, String startScore, String endScore);
	//2.3 search by the gift rank, because most interesting names are in high rank
	String getUidByName(String name);
	String getUserName(String keyUser);
	String getUserGiftSum(String keyUser);
	String getUserGiftSumByName(String name);
	String getUserGiftSum(long uid);
	//3. return list of givings-keys
	Set<String> givingTimeOriented(String timeStamp);
	
	//4.
	String getCountGift(String keyCountGift);
	String getCountGift(long gid);
	
	
	
}
