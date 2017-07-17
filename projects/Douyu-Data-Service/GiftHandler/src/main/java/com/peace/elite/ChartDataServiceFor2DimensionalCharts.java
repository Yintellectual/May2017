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
import com.peace.elite.chartService.entity.ExtendedCharEntry2D;
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
public class ChartDataServiceFor2DimensionalCharts extends ChartData<ChartEntry2D,ChartEntry2D>{


	private EventFactory<ChartEntry2D> partitions;
	
	public ChartDataServiceFor2DimensionalCharts(EventFactory<ChartEntry2D> partitions2d, String publishChannel, SimpMessagingTemplate webSocket){
		this.partitions = partitions2d;
		partitions.register(this);
		WEB_SOCKET_PUBLISH_CHANNEL = publishChannel;
		this.webSocket = webSocket;
	}

	@Override
	public ChartEntry2D clone(ChartEntry2D e){
		return e.clone();
	}
	
	@Override
	public void publish(ChartEntry2D e1, ChartEntry2D e2) {
		// TODO Auto-generated method stub
		publish(new Event<>(e2));
		if(e1 == null){
			
		}else{
			long oldData = e1.getData();
			e1.setData((-1)*oldData);
			publish(new Event<>(e1));
		}
	}
    public static ChartData2D getChartData(List<ChartEntry2D> data){    	
    	return new ChartData2D(
    			data.stream().filter(e->e!=null).map(e->e.getLabel()).collect(Collectors.toList()).toArray(new String[data.size()]), 
    			data.stream().filter(e->e!=null).map(e->e.getData()).collect(Collectors.toList()).toArray(new Long[data.size()]),
    			data.stream().filter(e->e!=null).map(e->e.getColor()).collect(Collectors.toList()).toArray(new String[data.size()]));
		
    }
	@Override
	public ChartData2D getChartData() {
		// TODO Auto-generated method stub
		return getChartData(chartEntries);
	}

	@Override
	//@Synchronized
	public void webSocketUpdate(int index, ChartEntry2D e) {
		// TODO Auto-generated method stub
		ChartUpdateData2D updateData;
		updateData = new ChartUpdateData2D(index, e.getLabel(), e.getData(), e.getColor() );
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/update", updateData);
	}
	public void useAsDataSource(){
		for(ChartEntry2D e:chartEntries){
			publish(new Event<>(e));
		}
	}
}
