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
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
@Component
@Configuration
public class RabbitMQProducerConfig{
	
	private RabbitMQConsumerConfig rabbitMQConsumerConfig;
	
	@Value("${queueName}")
	String queueName;
	
	@Bean
    Queue queue() {
        return new Queue(queueName, false);
    }
	
	@Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }
	
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }
	
	
}