package com.peace.elite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.peace.elite.chartService.chartData.ChartData;
import com.peace.elite.chartService.entity.ChartUpdateData2D;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.partition.Partitions2D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@Data
public class ChartDataServiceFor2DimensionalCharts extends EventFactory<Giving> implements ChartData<ChartEntry2D>, Listener<ChartEntry2D>{

	private ArrayList<ChartEntry2D> data;
	private EventFactory<ChartEntry2D> partitions;
	private String WEB_SOCKET_PUBLISH_CHANNEL;
	private SimpMessagingTemplate webSocket;
	
	public ChartDataServiceFor2DimensionalCharts(EventFactory<ChartEntry2D> partitions2d, String publishChannel, SimpMessagingTemplate webSocket){
		data = new ArrayList<>();
		this.partitions = partitions2d;
		partitions.register(this);
		WEB_SOCKET_PUBLISH_CHANNEL = publishChannel;
		this.webSocket = webSocket;
	}

	@Synchronized
	public void setAndSend(ArrayList<ChartEntry2D> data) {
		this.data = data;
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/init", getChartData());
	}
	
	private ChartEntry2D clone(ChartEntry2D entry){
		ChartEntry2D result = new ChartEntry2D(entry.getData(), entry.getLabel(), entry.getId());
		result.setColor(entry.getColor());
		return result;
	}
	//updates labels and data
	@Synchronized
	public void updateAndSend(ChartEntry2D e){
		ChartEntry2D entry = clone(e);
		ChartUpdateData2D updateData;
		long increasement = entry.getData();
		int index = data.indexOf(entry);
		if(index == -1){
			data.add(entry);
			index = data.indexOf(entry);
		}else{
			long oldMoney = data.get(index).getData();
			long newMoney = entry.getData();
			increasement = newMoney-oldMoney;
			System.out.println("\n\n\n\n\n\nincreasement("+increasement+") = newMoney("+newMoney+")-oldMoney("+oldMoney+");\n\n\n\n\n\n");
			data.set(index, entry);
		}
		updateData = new ChartUpdateData2D(index, entry.getLabel(), entry.getData(), entry.getColor() );
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/update", updateData);
		publish(new Event<Giving>(new Giving(Long.parseLong(entry.getId()), increasement, 0l, entry.getLabel())));
	}
	
	public ChartData2D getChartData(){
		return getChartData(data);
	}    		    
	
    public void sort(){
    	Collections.sort(data);
    	Collections.reverse(data);
	}
    public void sort(Comparator<ChartEntry2D> comparator){
    	Collections.sort(data, comparator);
    }
    
	public void reset(){
		data = new ArrayList<>();
	}
	
    public static ChartData2D getChartData(ArrayList<ChartEntry2D> data){    	
    	return new ChartData2D(
    			data.stream().filter(e->e!=null).map(e->e.getLabel()).collect(Collectors.toList()).toArray(new String[data.size()]), 
    			data.stream().filter(e->e!=null).map(e->e.getData()).collect(Collectors.toList()).toArray(new Long[data.size()]),
    			data.stream().filter(e->e!=null).map(e->e.getColor()).collect(Collectors.toList()).toArray(new String[data.size()]));
		
    }

    public void useAsDataSource(){
    	for(ChartEntry2D e:data){
    		publish(new Event<Giving>(new Giving(Long.parseLong(e.getId()), e.getData(), 0l, e.getLabel())));
    	}
    }
    
	@Override
	public void handle(Event<ChartEntry2D> e) {
		// TODO Auto-generated method stub
		updateAndSend(e.getData());
	}
}
