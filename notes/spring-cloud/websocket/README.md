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
	
		3.2.1 STOMP is frame based protocal, encoded in UTF-8.
		
		3.2.2 carriage returns, line feeds or colons are escaped.
		
			3.2.2.1 \r
			
			3.2.2.2 \n
			
			3.2.2.1 \c = :
			
			3.2.2.1 \\ = \
			
		3.2.3 undefined escape sequences are to be treated as error.
		
			3.2.3.1 Since STOMP 1.2, headers no longer be trimed or padded with spaces.
			
		3.2.4 only SEND, MESSAGE, and ERROR frames may have a body.

		3.2.5 conventional headers
		
			3.2.5.1 content-length
			
			3.2.5.2 content-type
			
			3.2.5.3 receipt
			
			3.2.5.1 content-length
			
		3.2.6 on repeated hearders, only the first header will be used. 
		
		3.2.7 server MAY limit the maximum length of list of headers, header lines, and the size of a frame body.
		
			3.2.7.1 on violation of the length limitation, server SHOULD send error frame and close the connection.
			
		3.2.8 the client may not receive the last frame sent by the server
		
	3.3 Connecting
		
		3.3.1 client: CONNECT accept-version:1.2 [1.0] [1.1] host:stomp.github.org 	[login:] [passcode:] [heart-beat:<send-millisec><receive-millisec>]
		
		server: CONNECTED version:1.2 [heart-beat:<send-millisec><receive-millisec>] [session:] [server:]
	
		3.3.2 on failure, server: ERROR error-message-in-body
		
		3.3.3 for heart-beat, 
			
			3.3.3.1 0 means never. 
			
			3.3.3.2 heart-beat only if both server and client want it
			
			3.3.3.3 heart-beat rate is the greater one of server and client
		
		3.3.4 if the send has no real STOMP frame to send during the heart-beat, then it is forced to send an EOL.
		
	3.4 Client Frames
	
		3.4.1 SEND
		
			SEND 
			destination:/queue/a
			content-type:text/plain
			[transcation:]
			[content-length:]
			
			hello queue accept-version
			^@
		
		3.4.2 SUBSCRIBE
		
			SUBSCRIBE
			id:0
			destination:/queue/foo
			ack:[client|auto|client-individual]
		
		3.4.3 UNSUBSCRIBE
		
		3.4.4 BEGIN
		
			BEGIN
			transcation:tx1
			
			^@
		
		3.4.5 COMMIT
			COMMIT
			transcation:tx1
			
			^@
		
		3.4.6 ABORT
			ABORT
			transcation:tx1
			
			^@
		
		3.4.7 ACK
			ACK
			id:12345
			transcation:tx1
		
		3.4.8 NACK
			NACK
			id:12345
			transcation:tx1
		
		3.4.9 DISCONNECT
	
	3.5 Server Frames
	
		3.5.1 MESSAGE
		
		MESSAGE
		subscription:0
		message-id:007
		destination:/queue/a
		content-type:text/plain
		
		hello queue a ^@
		
		3.5.2 RECEIPT
		
		3.5.3 ERROR
	
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
		
4. WebSocket API

	4.1 To create a WebSocket server is to implement WebSocketHandler, TextWebSocketHandler, or BinaryWebSocketHandler.
	
	4.2 Spring boot 1.5.3 auto registers a bean 
	 org.springframework.web.socket.messaging.SubProtocolWebSocketHandler
	
	
5. SockJS


6. sample code analysis

	https://github.com/spring-guides/gs-messaging-stomp-websocket/archive/master.zip
	https://spring.io/guides/gs/messaging-stomp-websocket/
	
	6.1 The project under "complete" is a simple message server.
	
	6.2 The server relies on the default beans injected by spring boot websocket.
	
	6.3 Configuration
	
		6.3.4 Extended AbstractWebSocketMessageBrokerConfigurer
		
		6.3.5 overrided configureMessageBroker 
		
			6.3.5.1 looks like it configures the receiving prefix as "/app" and sending(broker) prefix as "/topic"
			
		6.3.6 overrided registerStompEndpoints
		
			6.3.6.1 add endpoint "/gs-guide-websocket"
		
	6.4 Controller
	
		6.4.1 @MessageMapping "/hello"
		
		6.4.2 @SendTo "/topic/greetings"
		
		6.4.3 method argument HelloMessage 
		
		6.4.4 return a Greeting
			
	6.5 the default MessageConverter
	
		org.springframework.messaging.converter.CompositeMessageConverter
	
	6.6 in JS
	
		6.6.1 private setConnected
		
		6.6.2 public connect() --connect button
			
			connect to {endpoint}, subscribe "/topic/greetings" --defined in @SendTo
		
		6.6.3 public disconnect() --disc button
		
		6.6.4 public sendName() --send button
		
			send to /app/hello, which is handled by @MessageMapping 
	
		6.6.5 showGreeting(message) -- subscribe message handler
		
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
7. Annotation Message Handling

	7.1 @MessageMapping
		
		 Default: Ant-style, slash-separated, path patterns, e.g. "/foo*", "/foo/**".
	
	7.2 @DestinationVariable
	
		refers to /foo/{id}
		
	7.3 method arguments:
	
		7.3.1 Message
		
		7.3.2 default: @Payload, converted with a org.springframework.messaging.converter.MessageConverter
		
		7.3.3 @Header, converted with a 
		org.springframework.core.convert.converter.Converter
		
		7.3.4 @Headers that is assignable to java.util.Map
		
		7.3.5 MessageHeaders
		
		7.3.6 @DestinationVariable
		
		7.3.7 java.security.Principal
		
	7.4 a return value from an @MessageMapping method will be converted by 
			org.springframework.messaging.converter.MessageConverter
		and be used as the body of a new message, by default, to the same destination as the client message but using "/topic" prefix. 
		
	7.5 @SendTo 
	
		7.5.1 customized destination
		
		7.5.2 share common destination when in class level
		
	7.6 ListenableFuture/CompletableFuture/CompletionStage
	
	7.7 @SubscribeMapping
	
8. Sending Message with SimpMessagingTempalte

	8.1 convertAndSend

9. Authentication

10. UserDestinationMessageHandler

	Spring boot websocket provides
	
		org.springframework.messaging.simp.user.UserDestinationMessageHandler
	
	by default.

11. performance
http://blog.arungupta.me/rest-vs-websocket-comparison-benchmarks/

	Intuitively, websocket works faster than rest-api. However, the since the choice of protocals is considered architecture, the exact numbers and statistics are valuable. 
	
	Here is a study about the performance gap between websocket vs rest, or stomp vs http protocals.
	
	Stomp is bidirectional, full-duplex(??), single tcp connection, lean protocal. After handshaking, each stomp message can be as short as few bytes, while a http protocal usually contains headers of hundreds of characters. 
	
	Http may take 10 times longer than stomp to transfer the same amount of payload. Thanks to http headers, the shorter each message, the greater the performance gap.
	
	Infact, http is about 10 times slower for each message. However, if the message is very long, then the gap may go as small as 3 times. 
	
	This story tells that always choose websocket over http.
	
	

	
	
	
	
