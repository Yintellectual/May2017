package com.peace.elite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.entities.ChartEntry2D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@Service
@Controller
public class ChartDataServiceFor2DimensionalCharts {

	private ArrayList<ChartEntry2D> data;
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/generic";

	
	@Autowired
	SimpMessagingTemplate webSocket;

	@PostConstruct
	public void init(){
		data = new ArrayList<>();
	}
	
	public void setData(ArrayList<ChartEntry2D> data) {
		this.data = data;
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/init", getChartData());
	}
	
	//updates labels and data
	@Synchronized
	public void update(ChartEntry2D entry){
		ChartUpdateData2D updateData;
		int index = data.indexOf(entry);
		if(index == -1){
			data.add(entry);
			index = data.indexOf(entry);
		}else{
			data.set(index, entry);
		}
		
		
		updateData = new ChartUpdateData2D(index, entry.getLabel(), entry.getData(), entry.getColor() );
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/update", updateData);
		//Collections.sort(this.data);
    	//Collections.reverse(this.data);
	}
	private ChartData2D getChartData(){
		return getChartData(data);
	}    		    
    private ChartData2D getChartData(ArrayList<ChartEntry2D> data){    	
    	return new ChartData2D(
    			data.stream().filter(e->e!=null).map(e->e.getLabel()).collect(Collectors.toList()).toArray(new String[data.size()]), 
    			data.stream().filter(e->e!=null).map(e->e.getData()).collect(Collectors.toList()).toArray(new Long[data.size()]));
    }
	
    @MessageMapping("/2-dimensional/generic/init")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/init")
	public ChartData2D sendInitData(){
		return getChartData();
	}
//    
//    private long delta(int i, int j){
//    	return money(i)- money(j);
//    }
//    private long money(int i){
//    	return data.get(i).getData();
//    }
//    private String categoryLabel(long money){
//    	return "below "+ money;
//    }
//    private ChartEntry2D clone(int index){
//    	return new ChartEntry2D(
//				data.get(index).getData()
//				,data.get(index).getLabel().contains("Below")? data.get(index).getLabel():categoryLabel(data.get(index).getData())
//				,data.get(index).getLabel().contains("Below")? data.get(index).getLabel():categoryLabel(data.get(index).getData()));
//    }
//    public ChartData2D getCategorizedData(){
//		ArrayList<ChartEntry2D> result = new ArrayList<>();
//		ChartEntry2D currentColumn = clone(0);
//    	for(int i=1;i<data.size();i++){
//    		if(delta(i-1, i)>4*money(i)/10){
//    			result.add(currentColumn);
//    			currentColumn = clone(i);
//    		}else{
//    			currentColumn = currentColumn.addOn(data.get(i));
//    		}
//    	}
//    	result.add(currentColumn);
//    	this.data = result;
//    	return getChartData(result);
//    }
//    
    @MessageMapping("/2-dimensional/generic/sort")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/sort")
	public ChartData2D sortedDataAndSend(){
    	Collections.sort(data);
    	Collections.reverse(data);
		return getChartData();
	}
    
	public void reset(){
		init();
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class ChartUpdateData2D{
		private long index;
		private String label;
		private long data;
		private Long[] color;
	}
}
