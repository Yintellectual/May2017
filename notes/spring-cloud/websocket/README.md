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
	
3. STOMP protocal

	http://stomp.github.io/stomp-specification-1.2.html#Abstract
	
	3.1 Overview
	
		3.1.1 a stomp server is modelled as a set of destinations to which messages can be sent.
	
		3.1.2 a stomp client can send send frame or subscribe frame
	3.2 STOMP Frames
	
	3.3 Connecting
	
	3.4 Client Frames
	
	3.5 Server Frames
	
	3.6 Frames and Headers
	
	3.7 Augmented BNF (RFC 2616)
	
		command = client-command | server-command 
		
		client-command = "SEND"
						|"SUBSCRIBE"
						|"UNSUBSCRIBE"
						|"BEGIN"
						|"COMMIT"
						|"ABORT"
						|"ACK"
						|"NACK"
						|"DISCONNECT"
						|"CONNECT"
						|"STOMP"
						
		
		server-command = "CONNECTED"
                      | "MESSAGE"
                      | "RECEIPT"
                      | "ERROR"
					  
		frame = command EOL 
				*(header EOL)
				EOL
				*OCTET
				NULL
				*(EOL)
				
		frame-stream = 1*frame
		
		header = header-name ":" header-value
		header-name = 1*<any OCTET except CR or LF or ":">
		header-value = *<any OCTET except CR or LF or ":">