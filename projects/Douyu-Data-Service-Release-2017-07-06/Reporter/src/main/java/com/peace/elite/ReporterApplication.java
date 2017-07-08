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

import com.peace.elite.entities.ChatMessage;
import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.entities.SmallGift;
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
public class ReporterApplication {

	public Instant lastHeartBeat = Instant.now();
	@Autowired
	AudienceRedisRepository audienceRedisRepository;
	@Autowired
	RedisMQ redisMQ;
	@Autowired
	private SimpMessagingTemplate webSocket;
	public static void main(String[] args) {
		SpringApplication.run(ReporterApplication.class, args);
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

   @Component
   public class MyRunner implements CommandLineRunner {
		@Override
		public void run(String ... args) throws Exception{
				//connect(clientSocket);
		}
   }
   	
}
