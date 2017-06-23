http://docs.spring.io/spring-data/data-redis/docs/current/reference/html/

1. why spring data redis

2. start up 

3. redis support <interesting>

	3.1 Connection to Redis with Jredis connector
	
	3.2 working with Objects through RedisTemplate
	
		3.2.

	3.3 String-focused convenience classes, StringRedisTemplate
	
	3.4 RedisCallBack interface
	
	3.5 Hash mapping
	
		3.5.1 Intended for using redis hashes
		
		3.5.2 BeanUtilsHashMapper
		
		3.5.3 ObjectHashMapper
		
		3.5.4 Jackson2HashMapper
				
			3.5.4.1 based on FasterXML Jackson technology
			
				3.5.4.1.1 normal mapping
				
				3.5.4.1.2 flat mapping
	
	3.6 Messaging
		
		3.6.1 there is are channels in redis pubsub
		
		3.6.2 both RedisConnection and RedisTemplate offer publish method
		
		3.6.3 RedisMessageListenerContainer are used to handle message listeners
		
		3.6.4 subscribe to channels by using pattern matching is a good idea
		
		3.6.5 MessageListenerAdapter 
		
	3.7 Redis Transactions
	
		3.7.1 Transactions are controlled by multi(), exec(), and discard()
		
		3.7.2 @Transactional is disabled by default.
		
	3.8 Pipelining is supported
	
	3.9 Scripting is supported
	
	3.10 many interfaces defined in java are supported by redis in 
		org.springframework.data.redis.support
			
4. redis cluster

5. redis repositories <interesting>


