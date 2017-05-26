web UI:

	http://localhost:15672/#/ 


1. RabbitMQ is a Message Broker  

2. Message broker helps managing the complicity when many micro-applications couples with each other. 

3. Message broker helps applications using different technologies work together
	
4. Message broker provides scalability by reduce the complicity of communication in both message provider and consumer

5. Message broker is event driven

6. Message broker is a middle wear

7. RabbitMQ use AMQP to communicate with its consumers.

8. Here is a very good resource to start the study of RabbitMQ
		
		https://www.rabbitmq.com/getstarted.html
				
9. Terminalogies:

	Producing: sending message
	
			1. open a connection to broker
			
			2. make sure the desired queue exists. Create one if necessary
			
			3. a message is never be sent directly to a queue, but go through an exchange.
			
			4. provide information:
			
					exchange name
					
					routing_key
					
					message body
					
			5. message sent
			
	Exchange: an exchange receive data from producer and add it to some desired queue. 
	A producer may know about the existence of a queue,	but it never send message directly to any queue. 

			Types of Exchange:
			
				direct,
				
				topic,
				
				headers,
				
				fanout
	
	Queue: a large message buffer live within broker
	
	Consuming: waiting & receiving message

10. Work Queue:

	10.1 Work Queue is for parallelize task execution with many worker clients. 
	
	10.2 Message Acknowledgement is to prevent losing tasks
	
	10.3 Message Durability
	
	10.4 Fair dispatch
	