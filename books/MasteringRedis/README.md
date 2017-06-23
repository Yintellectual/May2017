Mastering Redis By Jeremy

purchased on kindle by my amazon.cn account: intellectual.y@gmail.com

0. Before The Hard Concepts

	0.1 This book is meant to install a foundation of becoming the 
	Redis Master Lord.
	
	0.2 The philosophy:
	
		0.2.1 BDL
		
		It is based on the Benevolent Dictator for Life(BDL) developing model.
		BDL is successful on redis, linux kernel and python.
		
		The dictator of redis is Sanfilippo.
		
		0.2.2 Seven Points
		
			0.2.2.1 DSL for abstract data types
			
				redis is a Domain Specific Language(DSL)?
					what is DSL?
			
			0.2.2.2 Memory Storage is the #1
		
				redis store all of the data in the RAM, which 
				is both fast and parallel across systems.
				
			0.2.2.3 Fundamental data structures for a fundamental API
			
				Redis has a fundamental api for its fundamental commands and data structures. 
				The whole redis API is built upon the fundamental api
				
			0.2.2.4 Code is like a poem
			
				Redis codes are narrative. 
				
			0.2.2.5 We're against complexity
			
				Some features may not be implemented in redis to avoid over-complexity
				
			0.2.2.6 Two levels of API
			
				Redis has a higher level api that supports redis master-slave and redis cluster
				
			0.2.2.7 We optimize for joy
			
				Redis cares about joy.
				
		0.2.3 What this book covers
		
			This book covers both Development and Operations of redis. However, the boundary between the two
			is becoming unclear. The topics covered in this book can be somewhere between development and operations which
			is called DevOps.
			
		0.2.4 Overviews
		
			0.2.4.1 Why reids?
			
				Free, fast and cloud friendly
			
			0.2.4.2 Advanced key management and data structures
			
				Knowledge that is necesary for designing tables in redis
				
			0.2.4.3 Managing RAM 
			
				How to save ram
				
			0.2.4.4 Programming Redis Part One
			
				Do I really need to reprogram a database??
				
			0.2.4.5 Part two
		
			0.2.4.6 Redis Cluster
			
			0.2.4.7 Redis and Complementary NoSQL Technologies
			
				?? isn't redis relational?
				
			0.2.4.8 Docker and Cloud
			
			0.2.4.9 Messaging Queue
			
			0.2.4.10 Measuring and Managing Information Streams
			
		0.2.5 Mastering Redis Open Badge
		
		0.2.6 Conventions
		
			0.2.6.1 There are codes, command-line inputs or outputs, notes and tips
			
		0.2.7 Example Codes
		
			0.2.7.1 http://www.packtpub.com/support
			
			0.2.7.2 https://github.com/PacketPublishing/Mastering-Redis
			
1. Why Redis
	
	1.0 the key features

	1.1 the pros
	
	1.2 the cons
			
	
2. Advanced Key Management and Data Structures 570

	2.1 Redis Keys
	
	2.2 Manually creating a redis schema
	
	2.3 Deconstructing a redis object mapper
	
	2.4 Big O notation
	
	2.5 Reviewing the time complexity of Redis data structures
	
	2.6 Sorted Sets
	
	2.7 Advanced sorted set operations
	
	2.8 Bitstrings and bit operations
	
	2.9 HyperLogLogs
	
3. Managing RAM 1146