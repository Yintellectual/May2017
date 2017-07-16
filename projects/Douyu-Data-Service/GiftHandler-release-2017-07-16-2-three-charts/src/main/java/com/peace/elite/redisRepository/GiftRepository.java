package com.peace.elite.redisRepository;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import com.peace.elite.entities.Giving;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Listener;

public abstract class GiftRepository implements Listener<Giving> {
	public static Function<Iterable<Giving>, Long> sumOfPrices = (givings) -> {
		Long sum=0l;
		
		for(Giving g:givings){
			sum = sum + getGiftPrice(g.getGid());
		}
		return sum;
	};
	public static Function<Iterable<Giving>, Long> sumOfGids = (givings) -> {
		Long sum=0l;
		
		for(Giving g:givings){
			sum = sum + g.getGid();
		}
		return sum;
	};
	public static BiPredicate<Giving, Giving> hasUid = (giving, user) -> {
		return user.getUid() ==giving.getUid();
	};
	public static BiPredicate<Giving, Long[]> withInTimeInterval = (giving, timeInterval) -> {
		return (giving.getTimeStamp() >= timeInterval[0]) && (giving.getTimeStamp() < timeInterval[1]);
	};
	public static Function<Long, String> timestampToShort = (timeStamp)->{
		Date localStartDate = new Date(timeStamp);
		DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
		String timeLabel = format.format(localStartDate);
		return timeLabel;
	};
	public static String test ="test";
	public static Function<Long[], String> timeIntervalToLabel = (timeInterval)->{
		return timestampToShort.apply(timeInterval[0]) + "-" + timestampToShort.apply(timeInterval[1]);
	};
	//1. 
	//void receive(String keyUser, String keyGift, long timeStamp, String userName);
	public abstract void receive(long uid, long gid, long timeStamp, String valueUserName);
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
	public abstract String getCountGiving(String keyGiving);
	public abstract String getCountGiving(long uid, long gid);
	public abstract long getGivingUid(String keyGiving);
	public abstract long getGivingGid(String keyGiving);
	//1.7 also count giving
	//void givingTimeLineUpdate(long timeStamp, String keyGiving);
	//void givingTimeLineUpdate(long timeStamp, long gid, long uid);
	
	//2. return list of uids, keyGift = [user:gift-rank:{gid}] [user:gift-rank:sum]

	//2.1 zrange
	public abstract Set<TypedTuple<String>> userGiftRankRankOriented(String keyGift, long startRank, long endRank);
	//2.2 zrangebyscore
	public abstract Set<TypedTuple<String>> userGiftRankScoreOriented(String keyGift, long startScore, long endScore);
	//2.3 search by the gift rank, because most interesting names are in high rank
	public abstract String getKeyUserByName(String name);
	public abstract String getUserName(String keyUser);
	public abstract String getUserName(long uid);
	public abstract String getUserGiftSum(String keyUser);
	public abstract String getUserGiftSumByName(String name);
	public abstract String getUserGiftSum(long uid);
	//3. return list of givings-keys
	public abstract Set<TypedTuple<String>> givingsTimeOriented(long start, long end);
	//4.
	public abstract String getCountGift(String keyCountGift);
	public abstract String getCountGift(long gid);
	
	//5.
	public static long getGiftPrice(long gid){
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

	public static String getGiftName(long gid) {
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
}
