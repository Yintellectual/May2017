Redis is popular these days. It is taged with the most popular concepts like
cloud, distribute, message queue, and in memory database. It is widely supported by
both the frameworks and the instructure providers. 

Default port : 6379

On windows, redis is installed as an service which can be managed in "task manager"> "service"

Data in redis is save because redis stores the data on disk before grace shutdown. 

Redis is fast. 1 million key-value pairs takes about 100mb memory. 

Redis store data in four structures:

	String, List, Hash and Set
	
	methods like leftPush and rightRange are common when manipulating lists. 
	
	hash is a set of keys that each maps to a set of key-value pairs. 
	
	set guarentees the uniqueness of the values, but not the ordering. 

https://redis.io/documentation
Knowledge on Redis:

	1. The full list of commands
	
		There are 200 commands according in the official site
		https://redis.io/commands
	
	2. Pipelining
	
	3. Redis Pub/Sub (Message Queue)
	
	4. Lua Scripting
	
	5. Debugging Lua Scripting
	
	6. Memory optimization
	
	7. Expires --? like dropping some data on expire?
	
	8. Redis as an LRU cache  --? what is LRU? : Less Recently Used
	
	9. Redis transactions : grouping commands together --? what is the difference 
	between pipelining and transaction?
	
	10. Mass insertion of data -- this would be powerful when doing backup and restore
	
	11. Partitioning -- distribute database
	
	12. Redis keyspace notifications --event trigger
	
	13. creating secondary indexes with redis  ??
	
Tutorials: Should be learned ASAP

Administration: 

	Redis-cli
	Configuration
	Replication
	Persistence
	Redis Administration
	Security
	Encryption
	Signal Handling
	Connection Handling
	High Availability??
	Latency Monitoring
	Benchmarks
	Redis Releases
	
Redis Cluster

Books:

	Mastering Redis  
	
	
--------------------2017-06-24----------

Commonly used commands

1. del -- delete by key

2. dump -- return the "real" data stored in memory

3. exists -- 1 for true and 0 for false

4. expire -- set expire time by seconds

5. expireat -- set expire time at unix timestamp like "1498263780"

6. pexpire -- by milliseconds

7. pexpireat -- milliseconds timestamp

8. keys -- find keys

9. move 

10. persist -- remove expiration from the key

11. pttl -- get the remining time of the key by milliseconds

12. ttl -- by seconds

13. randomkey -- random key

14. rename -- rename and override the new name

15. renamex -- rename if the new name does not exists

16. type -- get type of the key
Five Data Types in Redis

1. String

	1.1 commands
	
		1.1.1 set key value [ex seconds] [px milliseconds] [nx|xx]
		
		1.1.2 get 
		
		1.1.3 getrange key start end -- both ends inclusive
		
		1.1.4 getset key newValue
		
		1.1.5 getbit key offset -- get the bit at the position
		
		1.1.6 mget --multiple get
		
		1.1.7 setbit key offset -- can be super powerful if the math is rightRange
		
		1.1.8 setex -- set a value with expiration 
		
		1.1.9 
		
		https://www.tutorialspoint.com/redis/strings_setex.htm

2. Hash

	2.1 commands
	
		2.1.1 hmset
		
		2.1.2 hgetall
		
		

3. List

	3.1 commands 
	
		3.1.1 lpush
		
		3.1.2 lrange
4. Set
	
	4.1 commands 
	
		4.1.1 sadd
		
		4.1.2 smembers
		
5. Sorted Set

	5.1 commands
		
		5.1.1 zadd
		
		5.1.2 zrangebyscore
		



	
	