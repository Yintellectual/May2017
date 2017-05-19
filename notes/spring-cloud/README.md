http://cloud.spring.io/spring-cloud-static/Dalston.RELEASE/#_service_discovery_eureka_clients

http://www.baeldung.com/spring-cloud-netflix-eureka

Unlike the old style spring project, a spring cloud application consists of many microservices. 
Some are customly designed for business purpose, some are more like provided infrastructure. It can 
be tricky for beginners to spot the missing microservice that is compulsory for the whole project. 

1. Like the old style spring project, a spring cloud application usually need a DATABASE service. 

2. If the spring cloud application is using RabbitMQ, then RABBITMQ, and its dependency ERLANG, should be provided

3. If the spring cloud is using Eureka, then a EUREKA server should be provide. Unlike the database and rabbitMQ 
services, a Eureka server is not installed BUT provided by you. Writing an Eureka server is simply editing handful
lines to the initial spring-boot project. A working Eureka server can be found in:


	https://github.com/eugenp/tutorials/tree/master/spring-cloud/spring-cloud-eureka/spring-cloud-eureka-server
	
4. If the spring cloud is using spring-cloud-config, then a CONFIG server should be provided. This job is similar to 
providing a Eureka server, but need more configuration. Example can be found in:


	https://github.com/eugenp/tutorials/tree/master/spring-cloud/spring-cloud-config
	
	
	https://github.com/spring-cloud-samples/configserver