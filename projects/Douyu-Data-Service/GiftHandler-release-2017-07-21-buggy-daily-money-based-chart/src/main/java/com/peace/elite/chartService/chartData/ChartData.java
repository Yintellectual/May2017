package com.peace.elite.chartService.chartData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.peace.elite.ChartData2D;
import com.peace.elite.chartService.entity.ChartUpdateData2D;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;

import lombok.Data;
import lombok.Synchronized;

@Data
public abstract class ChartData<CHART_ENTRY extends Comparable<CHART_ENTRY>, RAW_ENTRY extends Comparable<RAW_ENTRY>> extends EventFactory<RAW_ENTRY> implements Listener<CHART_ENTRY>{
	
	protected ConcurrentSkipListSet<CHART_ENTRY> chartEntries = new ConcurrentSkipListSet<>();
	protected String WEB_SOCKET_PUBLISH_CHANNEL;
	protected SimpMessagingTemplate webSocket;
	
	//updates labels and data
	//@Synchronized
	public abstract void updateAndPublish(CHART_ENTRY entry);
	public abstract void publish(CHART_ENTRY e1, CHART_ENTRY e2);
	public abstract ChartData2D getChartData();
	public abstract ChartData2D getChartDataReverse();	    
	public abstract void webSocketUpdate(int index,CHART_ENTRY e);
	public abstract CHART_ENTRY clone(CHART_ENTRY e);
	public abstract void useAsDataSource();
	public abstract ChartData2D getChartDataCustomOrder(Comparator<CHART_ENTRY> comparator);
    
	public void reset(){
		chartEntries = new ConcurrentSkipListSet<>();
	}
	    
	@Override
	public void handle(Event<CHART_ENTRY> e) {
		// TODO Auto-generated method stub
		updateAndPublish(e.getData());
	}
	
	
}
