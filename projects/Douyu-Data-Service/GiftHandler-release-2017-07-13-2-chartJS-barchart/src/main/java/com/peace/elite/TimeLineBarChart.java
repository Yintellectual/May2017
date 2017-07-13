package com.peace.elite;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@Service
@Controller
public class TimeLineBarChart {

	private String[] labels;//user names
	private Long[] data;//rocket counters
	private ArrayList<String> timeLabels;
	private ArrayList<Long> money;
	
	@Autowired
	SimpMessagingTemplate webSocket;
	
	@PostConstruct
	public void init(){
		timeLabels = new ArrayList<String>();
		money = new ArrayList<Long>();
	}
	//updates labels and data
	@Synchronized
	public void update(long money, String timeLabel){
		
		BarChartUpdateData updateData;
			timeLabels.add(timeLabel);
			this.money.add(money);
			int index = timeLabels.indexOf(timeLabel);
			updateData = new BarChartUpdateData(index, timeLabel, money);
		
		webSocket.convertAndSend("/topic/barchart/timeline/update", updateData);
		labels = timeLabels.toArray(new String[timeLabels.size()]);
		data = this.money.toArray(new Long[timeLabels.size()]);
	}
	
    
	@MessageMapping("/barchart/timeline/init")
    @SendTo("/topic/barchart/timeline/init")
	public BarChartData sendInitData(){
		return new BarChartData(labels, data);
	}
	
	public void reset(){
		init();
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class BarChartUpdateData{
		private long index;
		private String name;
		private long counter;
	}
}
