package com.peace.elite.redisRepository;

import java.util.Set;

import com.peace.elite.entities.ChatMessage;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.entities.UserEnter;

public interface AudienceRedisRepository {

	void chatAdd(ChatMessage chatMessage);

	

	java.util.Map<String, String> audienceFetch(long uid);

	Set<String> moneyTop(int count);

	Set<String> chatTop(int count);

	Set<String> audienceChatFetch(long uid);

	Set<String> audienceMoneyFetch(long uid);

	long findUid(String name);

	void userEnter(UserEnter userEnter);

	void moneyAdd(SmallGift smallGift);

}
