package com.peace.elite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.redisMQ.RedisMQ;
import com.peace.elite.redisRepository.AudienceRedisRepository;
import com.peace.elite.redisRepository.GiftRepository;
import com.peace.elite.redisRepository.impl.GiftRepositoryRedisImpl;

import org.springframework.boot.CommandLineRunner;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.text.DateFormat;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@SpringBootApplication
public class GiftHandlerApplication {

	public Instant lastHeartBeat = Instant.now();
	@Autowired
	GiftRepository giftRepository;
	@Autowired
	RedisMQ redisMQ;
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private RocketBarChart rocketBarChart;
	@Autowired
	private TimeLineBarChart timeLineBarChart;
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;
	@Autowired
	private ChartDataServiceFor2DimensionalCharts chartService;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(GiftHandlerApplication.class, args);
	}
	
	@Bean ReceivingEventFactory receivingEventFactory(){
		return new ReceivingEventFactory();
	}
	
	public class ReceivingEventFactory extends EventFactory<SmallGift>{
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
	
//	@Bean
//	public Thread dataMiningThread(){
//		Thread miner = new Thread(()->{
//			final long fiveMinutes = 300000l;
//			final long startTime = 1499860800000l;
//			for(int i=0;i<100;i++){
//				long start = startTime+i*fiveMinutes;
//				long end = start + fiveMinutes;
//				Date localStartDate = new Date(start);
//				DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
//				String timeLabel = format.format(localStartDate);
//				
//				long money = 0l;
//				Set<TypedTuple<String>> timeline = giftRepositoryRedisImpl.givingsTimeOriented(start, end);
//				Pattern pattern = Pattern.compile(".*?:\\d*:(\\d*).*");
//				money = timeline.stream().map(tuple->tuple.getValue()).map(giving->{
//					Matcher matcher = pattern.matcher(giving);
//					if(matcher.matches()){
//						return matcher.group(1);
//					}
//					return null;
//				}).filter(gid->gid!=null)
//				.mapToLong(gid->Long.parseLong(gid))
//				.map(gid->giftRepositoryRedisImpl.getGiftPrice(gid))
//				.reduce(0, (l1, l2)->l1+l2);
//				
//				timeLineBarChart.update(money, timeLabel);
//			}
//		});
//		miner.start();
//		return miner;
//	}
	@Bean 
	public ThreadPoolExecutor threadPoolExecutor(){
		return new ThreadPoolExecutor(10, 20, 3600l, TimeUnit.SECONDS, new LinkedTransferQueue<>());
	}
	
	
	@Bean
	public Thread workingThread() {
		Thread worker = new Thread(() -> {
			while (true) {
				String message = redisMQ.messagePop(DouyuMessageType.dgb);
				Map<String, String> map = parseMessage(message);
				if (map != null) {
					SmallGift smallGift = SmallGift.getInstance(map);
					
					receivingEventFactory.publish(new Event<SmallGift>(smallGift));
					if(smallGift.getGfid()==196){
						rocketBarChart.update(smallGift.getUid(), smallGift.getNn());
					}
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
				threadPoolExecutor.execute(new DataMinor(1499860800000l, 1499889600000l, -1, giftRepository, chartService));
				
		}
   }
}
