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

import com.peace.elite.entities.BonusGift;
import com.peace.elite.entities.ChatMessage;
import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.redisMQ.RedisMQ;
import com.peace.elite.redisRepository.AudienceRedisRepository;


import org.springframework.boot.CommandLineRunner;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@SpringBootApplication
public class BonusHandlerApplication {

	public Instant lastHeartBeat = Instant.now();
	@Autowired
	AudienceRedisRepository audienceRedisRepository;
	@Autowired
	RedisMQ redisMQ;
	@Autowired
	private SimpMessagingTemplate webSocket;
	public static void main(String[] args) {
		SpringApplication.run(BonusHandlerApplication.class, args);
		}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
	    RedisTemplate<String, String> template = new RedisTemplate<>();
	    //start redis with --raw, and  cmd /K chcp 65001
	    template.setDefaultSerializer(new StringRedisSerializer());
	    template.setConnectionFactory(jedisConnectionFactory());
	    return template;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("localhost");
		jedisConFactory.setPort(6379);
		return jedisConFactory;
	}

	@Bean
	public Thread workingThread() {
		Thread worker = new Thread(() -> {
			while (true) {
				String message = redisMQ.messagePop(DouyuMessageType.bc_buy_deserve);
				BonusGift bonusGift= BonusGift.getInstance(parseMessage(message));
				audienceRedisRepository.moneyAdd(bonusGift);
				String display = bonusGift.toString();
				redisMQ.messageDisplay(display);
				try{
					if(display!=null)
						webSocket.convertAndSend("/topic/greetings", new Greeting(display));
				}catch(NullPointerException ue){
					//do nothing
				}
			}
		});
		worker.start();
		return worker;
	}

	public Map<String, String> parseMessage(String message) {
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
