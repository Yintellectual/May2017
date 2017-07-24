package com.peace.elite.chartService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.redisRepository.GiftRepository;

@Controller
public class RecordService implements Listener<Giving>{

	private final String WEB_SOCKET_APP_CHANNEL = "/greetings";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/greetings";
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	private RealTimeSender realTimeSender;
	
	class RealTimeSender implements Listener<Giving>{

		@Override
		public void handle(Event<Giving> e) {
			// TODO Auto-generated method stub
			if(GiftRepository.getGiftPrice(e.getData().getGid())>=1000){
				webSocket.convertAndSend("/topic/greetings", e.getData());
			}
		}
		
	}
	private ConcurrentSkipListSet<Giving> records = new ConcurrentSkipListSet<>((g1,g2)->(int)(g1.getTimeStamp()-g2.getTimeStamp()));
	
	@PostConstruct
	public void init(){
		attach(receivingEventFactory);
	}
	
	public void attach(EventFactory<Giving> source){
		records = new ConcurrentSkipListSet<>((g1,g2)->(int)(g1.getTimeStamp()-g2.getTimeStamp()));
		source.register(this);
	}
	@Override
	public void handle(Event<Giving> e) {
		// TODO Auto-generated method stub
		//webSocket.convertAndSend("/topic/greetings", e.getData());
		if(GiftRepository.getGiftPrice(e.getData().getGid())>=1000){
			records.add(e.getData());
		}
	}
	
	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/init")
	public void sendAll(){
		receivingEventFactory.remove(realTimeSender);
		realTimeSender = new RealTimeSender();
		Thread t = new Thread(()->{
			for(Giving g:records.toArray(new Giving[records.size()])){
				g.setUserName(g.getUserName());
				webSocket.convertAndSend("/topic/greetings", g);
				try{Thread.sleep(10);}catch(Exception e){}
			}
			receivingEventFactory.register(realTimeSender);
		});
		t.start();
	}
}
