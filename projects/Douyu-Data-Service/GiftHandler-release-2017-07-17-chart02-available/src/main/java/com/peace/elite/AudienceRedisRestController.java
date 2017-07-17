package com.peace.elite;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peace.elite.redisRepository.AudienceRedisRepository;

@Controller
@RequestMapping("/redis")
public class AudienceRedisRestController {

	@Autowired
	AudienceRedisRepository audienceRedisRepository;
	
	@ResponseBody
    @RequestMapping("/moneyTop/{count}")
    public Set<String> moneyTop(@PathVariable int count) throws Exception{
 		LinkedHashSet<String> result = new LinkedHashSet<>();
 		Set<String> uids = audienceRedisRepository.moneyTop(count);
 		for(String uid: uids){
 			result.add(substituteUidByProfile(uid));
 		}
 		return result;
    }
	
	private String substituteUidByProfile(String uid) throws Exception{
		Map<String, String> profile = audienceRedisRepository.audienceFetch(Long.parseLong(uid));
		String name = profile.get("name");
		String chatCount = profile.get("chat");
		String moneySum = profile.get("money");
		return String.format("name: %s, chat: %s, money: %s", name, chatCount, moneySum);
	}
	
	@ResponseBody
    @RequestMapping("/chatTop/{count}")
    public Set<String> chatTop(@PathVariable int count) throws Exception{
		LinkedHashSet<String> result = new LinkedHashSet<>();
 		Set<String> uids = audienceRedisRepository.chatTop(count);
 		for(String uid: uids){
 			result.add(substituteUidByProfile(uid));
 		}
 		return result;
	}
	
	@ResponseBody
    @RequestMapping("/chats/{uid}")
    public Set<String> chats(@PathVariable long uid) throws Exception{
		return audienceRedisRepository.audienceChatFetch(uid);
	}
	
	@ResponseBody
    @RequestMapping("/moneys/{uid}")
    public Set<String> moneys(@PathVariable long uid) throws Exception{
		return audienceRedisRepository.audienceMoneyFetch(uid);
	}
	
	@ResponseBody
    @RequestMapping("/findUid/{name}")
    public Long findUid(@PathVariable String name) throws Exception{
		return audienceRedisRepository.findUid(name);
	}
	
}
