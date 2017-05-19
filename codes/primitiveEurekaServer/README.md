This is a simple Eureka Server that is extremely easy to achieve:

	step 1: start.spring.io, create a project with Eureka Server dependency
	
	step 2: add org.springframework.cloud.netflix.eureka.server.EnableEurekaServer as annotation to com.peaceelite.primitiveEurekaServer.PrimitiveEurekaServerApplication;
	
	step 3: rename application.properties to application yml, and add
	
		server:
			port: 8761
		eureka:
			client:
			registerWithEureka: false
			fetchRegistry: false
			
	to it.
	
mvn spring-boot:run to run it, and localhost:8761 to see it. 
			