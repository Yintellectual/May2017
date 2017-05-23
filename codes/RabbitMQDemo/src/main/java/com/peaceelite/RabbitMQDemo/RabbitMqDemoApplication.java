package com.peaceelite.RabbitMQDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
@SpringBootApplication
public class RabbitMqDemoApplication {
	
    final static String queueName = "spring-boot";

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private Receiver receiver;
	@Autowired
	private ConfigurableApplicationContext context;
	
	private void produceMessage(String message){
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend("spring-boot", message);
	}
	

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqDemoApplication.class, args);
	}
	
	
	@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

	
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

	
	@Bean
	public CommandLineRunner run(RabbitTemplate rabbitTemplate, Receiver receiver, ConfigurableApplicationContext context) throws Exception {
		return args -> {
			for(String arg:args){
				System.out.println("\n\n\n\n"+arg+"\n\n\n\n");
				
			}
	
			if("sender".equals("sender")){
				
				while(true){
					
					String instantTime = Instant.now().toString();
					produceMessage(instantTime);
					Thread.sleep(3000);
				
				}
			}else if (args[0].equals("receiver")){
				
				
				
			}
			
	
		};
	}
	
}
