"https://www.mkyong.com/spring-boot/spring-boot-deploy-war-file-to-tomcat/" 

1. to deploy a spring-boot project in external tomcat/

	1.1 make Application class extend org.springframework.boot.web.support.SpringBootServletInitializer;
	
	1.2 	<!-- marked the embedded servlet container as provided -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	
	1.3 <packaging>war</packaging>
	
	
2. to use tomcat 8

	<properties>
		<tomcat.version>8.5.9</tomcat.version>
	</properties>