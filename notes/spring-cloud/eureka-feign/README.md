http://www.baeldung.com/spring-cloud-netflix-eureka

Eureka feign is an alternate of restTemplate when Eureka is used. 

	Step 1:	Feign dependency
	
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-feign</artifactId>
			<version>1.1.5.RELEASE</version>
		</dependency>
		
	Step 2: mark the Application as EnableFeignClients
	
	Step 3: Make an interface for feign. This interface provides the relationship from its methods to restful api
	
		3.1 Annotate the Interface as @FeignClient(<eureka-application-name>)
		
		3.2 Annotate a method as  @RequestMapping("/greeting"), where "/greeting" is from the other application's API
		
	Step 4: To use feign, simply use the interfaces made for it.
	
		4.1 Autowired the interface
		
		4.2 call the interface method to get infomation from remote restful application.