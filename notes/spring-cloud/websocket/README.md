We need a real time dialog with the browser. WebSocket supported by Spring may help. 

1. Overview
	
	STOMP supported. 
	
		Question: what is STOMP? isn't it the email protocal?
		https://stomp.github.io/
		STOMP 1.2 released on 10/22/2012
		Very easy to implement.
		
	WebSocket is better than socket and socketIO
	
	WebSockets are TCP for the web.
	
2. Getting Started
	https://spring.io/guides/gs/messaging-stomp-websocket/
	
	
	2.1 the message is passed as json
	
	2.2 message controller
	
	import org.springframework.messaging.handler.annotation.MessageMapping;
	import org.springframework.messaging.handler.annotation.SendTo;
	import org.springframework.stereotype.Controller;
	
	The method returns an object that then be parsed into json
	
	2.3 
	import org.springframework.context.annotation.Configuration;
	import org.springframework.messaging.simp.config.MessageBrokerRegistry;
	import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
	import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
	import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

	@Configuration
	@EnableWebSocketMessageBroker
	public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	2.4
	client