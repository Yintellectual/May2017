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
	
	