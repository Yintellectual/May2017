package com.zhiyuninfo.dm.client.utils;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhiyuninfo.dm.client.InputParams;
import com.zhiyuninfo.utility.domain.Device;

@Component
public class MQSender {
    protected Logger logger = LoggerFactory.getLogger(getClass());
	private static ObjectMapper mapper = new ObjectMapper();

	/*public void asynSend(String destination, Object request) {
		jmsQueueTemplate.send(destination, new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {
				String content= new String();
				try {
					content = mapper.writeValueAsString(request);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				}
				TextMessage textMessage = session.createTextMessage(content);
                return textMessage;
			}
		} );
		logger.info(String.format("请求消息已发至队列-[%s], 消息：[%s]", destination, request));
	}*/
	
	@Autowired
	private InputParams params;
	
	private ConnectionFactory connFactory;  
    private Connection conn;  
    private Session session;  
    private MessageProducer producer;  
  
    public void asynSend(String queueName, Device device) throws Exception {
    	
    	initConn();
    	
        // 创建目标，就创建主题也可以创建队列  
        Destination destination = session.createQueue(queueName);  
  
        // 消息生产者  
        producer = session.createProducer(destination);  
        // 设置持久化，DeliveryMode.PERSISTENT和DeliveryMode.NON_PERSISTENT  
        // 如果DeliveryMode没有设置或者设置为NON_PERSISTENT，那么重启MQ之后消息就会丢失。  
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);// 持久化  
  
        // 发送消息  
        sendText(session, producer, device);
        session.commit();// 在事务会话中，只有commit之后，消息才会真正到达目的地  
        logger.info("已发送消息至队列：" + queueName + ", 消息内容：" + device.toString());
    } 
    
    private void destory() throws JMSException {
    	if (producer!=null){
    		producer.close();  
    	}
    	if (session!=null) {
    		session.close();  
    	}
    	if (conn!=null) {
    		conn.close(); 
    	}
    }
    
    private void initConn() {
    	if (connFactory == null) {
    		try {
				// 连接工厂  
				// 设置用户名和密码，这个用户名和密码在conf目录下的credentials.properties文件中  
				connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, params.getBrokerUrl());  
				// 连接到JMS提供者  
				conn = connFactory.createConnection();  
				conn.start();  
     
				// 事务性会话，自动确认消息  
				// 第一个参数是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。  
				// 第二个参数消息的确认模式：  
				// AUTO_ACKNOWLEDGE ：  
				// 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中可能因为错误而丢失消息。  
				// CLIENT_ACKNOWLEDGE ：  
				// 由消息接收者确认收到消息，通过调用消息的acknowledge()方法（会通知消息提供者收到了消息）  
				// DUPS_OK_ACKNOWLEDGE ： 指定消息提供者在消息接收者没有确认发送时重新发送消息(这种确认模式不在乎接收者收到重复的消息）  
				session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				try {
					destory();
				} catch (JMSException e1) {
					logger.error(e.getMessage(),e1);
				} finally {
					initConn();
				}
			}  
    	}
    }
  
    // 字节消息  
    public void sendBytes(Session session, MessageProducer producer, byte[] bytes)  
            throws JMSException {  
        BytesMessage bytesMessage = session.createBytesMessage();  
        bytesMessage.writeBytes(bytes);  
        producer.send(bytesMessage);  
    }  
  
    // 流消息  
    public void sendStream(Session session, MessageProducer producer)  
            throws JMSException {  
        StreamMessage streamMessage = session.createStreamMessage();  
        streamMessage.writeString("streamMessage流消息");  
        streamMessage.writeLong(55);  
        producer.send(streamMessage);  
    }  
  
    // 键值对消息  
    public void sendMap(Session session, MessageProducer producer)  
            throws JMSException {  
        MapMessage mapMessage = session.createMapMessage();  
        mapMessage.setLong("age", 25);  
        mapMessage.setDouble("sarray", new Double(6555.5));  
        mapMessage.setString("username", "键值对消息");  
        producer.send(mapMessage);  
    }  
  
    // 文本消息  
    public void sendText(Session session, MessageProducer producer, Device device)  
            throws JMSException {
    	String content= new String();
		try {
			content = mapper.writeValueAsString(device);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(),e);
		}
        TextMessage textMessage = session.createTextMessage(content);  
        producer.send(textMessage);  
    }
}
