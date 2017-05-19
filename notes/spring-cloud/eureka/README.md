Generally speaking, eureka is a public registration sheet for all microservices that based on it. 

To use Eureka, 

	- first set up an Eureka server. This step is mentioned in May2017/notes/spring-cloud/README.md.

	- After then, the application need to be marked as @EnableEurekaClient. 

	- Now, EurekaClient bean should be accessed in order to get information about microservices. 

Common methods and usage of related classes are listed here:

com.netflix.discovery.EurekaClient

	getApplication(String name)
	
	getApplications(String name)
	
com.netflix.discovery.shared.Application

	getInstances
	
com.netflix.appinfo.InstanceInfo

	getHostName()
	getPort()

	
Once hostname and port is obvious, if Restful server it is, then restTemplate or eureka-feign can be used to retieve data. 

About Eureka-feign, see another topic under the name.
