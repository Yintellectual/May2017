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
		
		1.1.9 setnx	-- set value and create a new key if necessary
		
		1.1.10 setrange key_name offset value -- could be super powerful, just like setbit
		
		1.1.11 strlen -- return 0 if the value does not exists
		
		1.1.12 mset -- multiple set
		
		1.1.13 msetnx -- perform only if none of the keys exists
		
		1.1.14 psetex -- set expiration by milliseconds
		
		1.1.15 incr -- increase integer by 1
		
		1.1.16 incrby key num
		
		1.1.17 incrbyfloat -- perform on floats. create key if necessary. nil value is treated as 0
		
		1.1.18 decr
		
		1.1.19 decrby
		
		1.1.20 append
		
		* note that there is no split or insert

2. Hash

	2.1 commands
	
		2.1.1 hmset
		
		2.1.2 hgetall
		
		2.1.3 hdel 
		
		2.1.4 hexists key field
		
		2.1.5 hget key field
		
		2.1.6 hgetall 
		
		2.1.7 hincrby
		
		2.1.8 hincrbyfloat
		
		2.1.9 hkeys -- return field names
		
		2.1.10 hlen -- count of fields
		
		2.1.11 hmget -- hash multiple get
		
		2.1.12 hmset -- hash multiple set
		
		2.1.13 hset 
		
		2.1.14 hsetnx -- set only if field does not exist
		
		2.1.15 hvals -- get all the values		

3. List

	3.1 commands 
	
		3.1.1 lpush
		
		3.1.2 lrange
		
		3.1.3 blpop -- block lefl pop
		
		3.1.4 brpop -- block right pop 
		
		3.1.5 brpoplpush list1 list2 timeout -- brpop list1 and lpush list2
		
		3.1.6 lindex -- get by index from leftside. the index starts from 0
		
		3.1.7 linsert key before/after pivot value -- left insertion, note that pivot is an existing value
		
		3.1.8 llen
		
		3.1.9 lpop
		
		3.1.10 lpush
		
		3.1.11 lpushx -- only if the key exists and holds a list
		
		3.1.12 LRANGE
		
		3.1.13 lrem key count value -- remove by value. the total number of removal is set by count. if count >0 then start from head, else if count <0 then start from tail, 
		if count = 0 then remove all.
		
		3.1.14 lset key index value
		
		3.1.15 ltrim
		
		3.1.16 rpop
		
		3.1.17 rpoplpush
		
		3.1.18 rpush
		
		3.1.19 rpushx  -- restrictively right push
		
4. Set
	
	4.1 commands 
	
		4.1.1 sadd
		
		4.1.2 smembers
		
		4.1.3 scard -- count
		
		4.1.4 
	
	https://www.tutorialspoint.com/redis/strings_setex.htm
	
5. Sorted Set

	5.1 commands
		
		5.1.1 zadd
		
		5.1.2 zrangebyscore
		
	
	


	
	