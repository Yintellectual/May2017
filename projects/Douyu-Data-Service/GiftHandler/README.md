Redis Database Schema

Key Space					Type			Discription				Purpose
all start with project name "douyu-db"
1. user:{uid}					hash			- name
											- gift-total
											
2. gift:{gid}					hash			- name
(rocket, jet, card and packaged small gifts)- price
									
3. giving:{uid}:{gid}			string			count
											
									
4. givings:time-line			zset			value is giving key:unix-timestamp
											score is unix-timestamp
									
5. users:gift-rank:total		zset			value is uid
											score is gift-total
									
6. users:gift-rank:{gift id}	zset			value is uid
											score is gift count of the type
											
7. count:gift:{gift id}		string			


8. global:gifts				set


9. global:users				set


10.count:users 				String 

repository interface:

	0.	hmset gifts
	
		0.1 Gifts
		
		0.2 Hmset gifts

	1. 	receive(User, Gift, timestamp)

		1.1 
			sadd		User.id
			hset 		User.name
			hincrby 	User.gift-sum	Gift.price
		
		1.2 zincrby user:gift-rank:sum Gift.price User.id
		
		1.3 zincrby user:gift-rank:{Gift.id} 1 User.id
		
		1.4 incr	count:gift:{Gift.id}
		
		1.5 incr	global:giving:{uid}:{gid}
		
		1.6 zadd	givings:time-line timestamp global:giving:{uid}:{gid}
		
	2.	rank list: rocket
	
		zrange
		
	3.	rank list: jet
	
		zrange
		
	4.	rank list: gift-sum
	
		zrange 
		
	5.	recent rockets start-time
	
		zrevrange 
		
		java filter
		
	6.	recent jet start-time
	
		zrevrange
		
		java filter
		
	7.	recent gift-id uid start-time
	
		zrevrange
		
		java filter
		
	8.	total rocket
	
		get
		
	9.	total jet
	
		get

	10. total gift price
	
		get get get 
			
	11.	user gift report
	
		11.1 sum & rank
		
		11.2 rocket count & rank
		
		11.3 jet count & rank
		
		11.4 card count & rank
		
		11.5 recent gift sum 
		
			zrevrange
			
			java filter
	
		

