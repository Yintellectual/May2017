package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	AudienceRepository audienceRepository;
	

	@Bean
	public RedisTemplate<String, Audience> redisTemplate() {
	    RedisTemplate<String, Audience> template = new RedisTemplate<>();
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
	
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(){
		return (String ... args)->{
//			
//			Audience peter= new Audience();
//			peter.setName("peter");
//			peter.setUid(3213124L);
//			peter.setMoney(6);
//			peter.setChat(0);
//			peter.setTime(0);
			System.out.println("money sum = "+audienceRepository.moneySum());
			
//			audienceRepository.moneyIncrBy(peter.getUid(), 500);
//			for(int i=0;i<20;i++){
//				Thread.sleep(20);
//				audienceRepository.chatRecord(peter.getUid(), "hello "+i);
//			}
		};
	}

}
