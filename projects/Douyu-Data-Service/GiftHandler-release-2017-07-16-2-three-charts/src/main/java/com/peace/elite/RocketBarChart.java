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
public class RocketBarChart {

	private String[] labels;//user names
	private Long[] data;//rocket counters
	private ArrayList<String> names;
	private ArrayList<Long> uids;
	private ArrayList<Long> rocketCounters;
	
	public static String UNKNOWN(long uid){
		return "用户"+uid; 
	} 
	@Autowired
	SimpMessagingTemplate webSocket;
	
	@PostConstruct
	public void init(){
		names = new ArrayList<String>();
		uids = new ArrayList<Long>();
		rocketCounters = new ArrayList<Long>();
	}
	//updates labels and data
	@Synchronized
	public void update(long uid, String name){
		if(name==null||name.isEmpty()){
			name = UNKNOWN(uid);
		}
		BarChartUpdateData updateData;
		int index = uids.indexOf(uid);
		if(index == -1){
			uids.add(uid);
			names.add(name);
			rocketCounters.add(1l);
			index = uids.indexOf(uid);
			updateData = new BarChartUpdateData(index, name, rocketCounters.get(index));
		}else{
			names.set(index, name);
			long counter = rocketCounters.get(index);
			rocketCounters.set(index, counter+1);
			updateData = new BarChartUpdateData(index, name, counter+1);
		}
		
		webSocket.convertAndSend("/topic/update", updateData);
		labels = names.toArray(new String[uids.size()]);
		data = rocketCounters.toArray(new Long[uids.size()]);
	}
	
    
	@MessageMapping("/init")
    @SendTo("/topic/init")
	public ChartData2D sendInitData(){
		//return new ChartData2D(labels, data);
		return null;
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
