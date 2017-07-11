package com.peace.elite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.redisMQ.RedisMQ;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.CommandLineRunner;
import java.time.*;
import java.net.*; 

import org.springframework.beans.factory.annotation.Autowired;
import java.util.logging.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
@Controller
@SpringBootApplication
public class MonitorApplication {

	
	private static final Logger LOGGER = Logger.getLogger( MonitorApplication.class.getName() );
	public Instant lastHeartBeat = Instant.now();
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	RedisMQ redisMQ;
	
	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
		}
	private SocketAddress address;
/* 
	@ResponseBody
    @RequestMapping(name="/greeting")
    public String greeting(RestTemplate restTemplate) throws Exception{
 		//String result = new String (
		//	restTemplate.getForObject("http://api.live.bilibili.com/gift/getTop?roomid=159586&_=1493738109671", String.class)
		//	, "GBK");
		
		//String result = restTemplate.getForObject("http://api.live.bilibili.com/gift/getTop?roomid=1", String.class);
		String url = "";
	
        return "dada";//restTemplate.getForObject(url, String.class);
    }
 */	
	
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
	
	
   @Component
   public class MyRunner implements CommandLineRunner {
		@Override
		public void run(String ... args) throws Exception{
				//connect(clientSocket);
		}
   }
   
   
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	

	
	@Bean
	public Thread stageThread() {
		Thread worker = new Thread(()->{
			while(true){
				String display = redisMQ.messageStage();
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
	
	
	
	
	//1. The new datahandler methods return no exception (It deal with all its exceptions locals)
	
	//2. better filter that allows chat, super chat, super user in, and good gifts
	
	//		"chatmsg" "spbc".equals(map.get("type")) "bc_buy_deserve"
	
	//3. an socket web service 
	
	//4. allows user to change filter settings
}
