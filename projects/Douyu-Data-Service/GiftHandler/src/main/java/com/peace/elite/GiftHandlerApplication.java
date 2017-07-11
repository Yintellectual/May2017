package com.peace.elite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.redisMQ.RedisMQ;
import com.peace.elite.redisRepository.AudienceRedisRepository;


import org.springframework.boot.CommandLineRunner;
import java.util.*;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@SpringBootApplication
public class GiftHandlerApplication {

	public Instant lastHeartBeat = Instant.now();
	@Autowired
	AudienceRedisRepository audienceRedisRepository;
	@Autowired
	RedisMQ redisMQ;
	@Autowired
	private SimpMessagingTemplate webSocket;
	public static void main(String[] args) {
		SpringApplication.run(GiftHandlerApplication.class, args);
	}

	@Bean
	public Thread workingThread() {
		Thread worker = new Thread(() -> {
			while (true) {
				String message = redisMQ.messagePop(DouyuMessageType.dgb);
				Map<String, String> map = parseMessage(message);
				if (map != null) {
					SmallGift smallGift = SmallGift.getInstance(map);
					audienceRedisRepository.moneyAdd(smallGift);
					String display = smallGift.toString();
					redisMQ.messageDisplay(display);
					try {
						if (display != null)
							webSocket.convertAndSend("/topic/greetings", new Greeting(display));
					} catch (NullPointerException ue) {
						// do nothing
					}
				}
			}
		});
		worker.start();
		return worker;
	}

	public Map<String, String> parseMessage(String message) {
		if(!message.contains("dw@=1")){
			System.out.println(message);
			return null;
		}
		Map<String, String> protocal = new LinkedHashMap<>();
		String[] entries = message.split("/");
		for (String entry : entries) {
			entry = entry.replaceAll("@S", "/");
			String[] pair = entry.split("@=");
			String[] result = new String[2];
			try {
				for (int i = 0; i < 2; i++) {
					result[i] = pair[i].replaceAll("@A", "@");
				}
				protocal.put(result[0], result[1]);
			} catch (ArrayIndexOutOfBoundsException e) {// not key-value pair,
														// must be a header or a
														// bad data
				// do nothing
			}
		}
		return protocal;
	}	

   @Component
   public class MyRunner implements CommandLineRunner {
		@Override
		public void run(String ... args) throws Exception{
				//connect(clientSocket);
		}
   }
   	
}
