"http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html" 


1. Configure thymeleaf in spring:

	package com.example.demo;

	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.thymeleaf.TemplateEngine;
	import org.thymeleaf.spring4.SpringTemplateEngine;
	import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
	import org.thymeleaf.templateresolver.TemplateResolver;
	@Configuration
	public class WebConfig {

		@Bean
		public TemplateResolver templateResolver(){
			TemplateResolver templateResolver = new ServletContextTemplateResolver();
			templateResolver.setPrefix("/WEB-INF/templates/");
			templateResolver.setSuffix(".html");
			templateResolver.setTemplateMode("HTML5");
			return templateResolver;
		}
		@Bean
		public TemplateEngine templateEngine(TemplateResolver templateResolver){
			TemplateEngine templateEngine = new SpringTemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);
			return templateEngine;
		}
	}

	
2. There maybe some problems on spring-boot dependency starter, if so, do manual dependency injection

3. Thyleaf requires the html to be restrictly XML, or, have all tags paired and closed properly

4. Replacement

	<div th:replace="fragment/header :: header"> 
	
	in webapp/WEB-INF/templates/fragment/header.html
	<div th:fragment="navbar">
