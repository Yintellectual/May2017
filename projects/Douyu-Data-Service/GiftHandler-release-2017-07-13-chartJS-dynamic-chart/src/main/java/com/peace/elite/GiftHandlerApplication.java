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
import com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl;

import org.springframework.boot.CommandLineRunner;
import java.util.*;
import java.util.stream.IntStream;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@SpringBootApplication
public class GiftHandlerApplication {

	public Instant lastHeartBeat = Instant.now();
	@Autowired
	GiftRepositoryRedisImpl giftRepositoryRedisImpl;
	@Autowired
	RedisMQ redisMQ;
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private RocketBarChart rocketBarChart;
	
	public static void main(String[] args) {
		SpringApplication.run(GiftHandlerApplication.class, args);
	}
	
	
//	@Bean 
//	public Thread testThread(){
//		Thread test = new Thread(()->{
//			
//			
//			Random random = new Random();
//			final int UPDATES = 10000;
//			final int USERS = 100;
//			IntStream.range(0, UPDATES).forEach(l-> {
//				long uid = random.nextInt(USERS);
//				rocketBarChart.update(uid, "User "+uid);
//				try{Thread.sleep(200);}catch(Exception e){}
//			});
//			
//		});
//		test.start();
//		return test;
//	}
	
	@Bean
	public Thread workingThread() {
		Thread worker = new Thread(() -> {
			while (true) {
				String message = redisMQ.messagePop(DouyuMessageType.dgb);
				Map<String, String> map = parseMessage(message);
				if (map != null) {
					SmallGift smallGift = SmallGift.getInstance(map);
					
					
					giftRepositoryRedisImpl.receive(smallGift.getUid(),smallGift.getGfid(), new Date().getTime(), smallGift.getNn());
					if(smallGift.getGfid()==196){
						rocketBarChart.update(smallGift.getUid(), smallGift.getNn());
					}
					//String display = smallGift.toString();
					//redisMQ.messageDisplay(display);
					try {
						//if (display != null)
						//	webSocket.convertAndSend("/topic/greetings", new Greeting(display));
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
