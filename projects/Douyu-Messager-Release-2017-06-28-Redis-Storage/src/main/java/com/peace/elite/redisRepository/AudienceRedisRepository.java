package com.peace.elite.redisRepository;

import com.peace.elite.entities.ChatMessage;
import com.peace.elite.entities.SmallGift;

public interface AudienceRedisRepository {

	void chatAdd(ChatMessage chatMessage);

	void moneyAdd(SmallGift bigGift);
}
