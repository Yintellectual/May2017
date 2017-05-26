package com.peaceelite.RabbitMQDemo;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    private final ConfigurableApplicationContext context;

	@Value("${queueName}")
	String queueName;
	
    public Runner(RabbitTemplate rabbitTemplate,
            ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
        context.close();
    }

}