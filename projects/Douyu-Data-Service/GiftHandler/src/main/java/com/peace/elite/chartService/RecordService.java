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
public class RecordService implements Listener<Giving> {

	private final String WEB_SOCKET_APP_CHANNEL = "/greetings";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/greetings";

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	@Autowired
	private DailyMoneyBasedChart moneyBasedChart;
	@Autowired
	private DailyTimeBasedCharts timeBasedCharts;
	private RealTimeSender realTimeSender;
	private Thread t;
	private EventFactory<Giving> source;

	class RealTimeSender implements Listener<Giving> {

		int i = 1;

		@Override
		public void handle(Event<Giving> e) {
			// TODO Auto-generated method stub
			if (GiftRepository.getGiftPrice(e.getData().getGid()) >= 1000) {
				webSocket.convertAndSend("/topic/greetings", e.getData());
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof RealTimeSender)) {
				return false;
			}
			return true;
		}

	}

	private ConcurrentSkipListSet<Giving> records = new ConcurrentSkipListSet<>(
			(g1, g2) -> (int) (g1.getTimeStamp() - g2.getTimeStamp()));

	@PostConstruct
	public void init() {
		source = receivingEventFactory;
		attach(source);
	}

	public void attach(EventFactory<Giving> source) {
		synchronized (this) {
			if(timeBasedCharts!=null)
			if(timeBasedCharts.getGivingDataSource()!=null)
				timeBasedCharts.getGivingDataSource().remove(this);
			if(moneyBasedChart!=null)
			if(moneyBasedChart.getGivingDataSource()!=null)
			moneyBasedChart.getGivingDataSource().remove(this);
			source.remove(this);
			this.source = source;
			records = new ConcurrentSkipListSet<>((g1, g2) -> (int) (g1.getTimeStamp() - g2.getTimeStamp()));
			this.source.register(this);
		}
	}

	@Override
	public void handle(Event<Giving> e) {
		// TODO Auto-generated method stub
		// webSocket.convertAndSend("/topic/greetings", e.getData());
		if (GiftRepository.getGiftPrice(e.getData().getGid()) >= 1000) {
			records.add(e.getData());
		}
	}

	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/init")
	public void sendAll(EventFactory<Giving> source) {
		synchronized (this) {
			if(timeBasedCharts!=null)
			if(timeBasedCharts.getGivingDataSource()!=null)
			timeBasedCharts.getGivingDataSource().remove(realTimeSender);
			if(moneyBasedChart!=null)
			if(moneyBasedChart.getGivingDataSource()!=null)
			moneyBasedChart.getGivingDataSource().remove(realTimeSender);
			this.source.remove(realTimeSender);
			realTimeSender = new RealTimeSender();
			this.source = source;
			this.source.register(realTimeSender);
			t = new Thread(() -> {
				for (Giving g : records.toArray(new Giving[records.size()])) {
					g.setUserName(g.getUserName());
					webSocket.convertAndSend("/topic/greetings", g);
					try {
						Thread.sleep(10);
					} catch (Exception e) {
					}
				}
			});
			t.start();
		}

	}
}
