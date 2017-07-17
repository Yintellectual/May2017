package com.peace.elite.chartService.chartData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
public abstract class ChartData<CHART_ENTRY extends Comparable<CHART_ENTRY>, RAW_ENTRY> extends EventFactory<RAW_ENTRY> implements Listener<CHART_ENTRY>{
	
	protected CopyOnWriteArrayList<CHART_ENTRY> chartEntries = new CopyOnWriteArrayList<>();
	protected String WEB_SOCKET_PUBLISH_CHANNEL;
	protected SimpMessagingTemplate webSocket;
	
	//updates labels and data
	//@Synchronized
	public void updateAndPublish(CHART_ENTRY entry) {
		int index = -1;
		for(CHART_ENTRY e:chartEntries){
			if(e==entry){
				 index = chartEntries.indexOf(e);
				 //minus the first one, and leave the second one be zero
				 //publish(e,e);
			}
		}
		if(index == -1){
			chartEntries.add(entry);
			index = chartEntries.indexOf(entry);
		}
		webSocketUpdate(index, entry);
	}
	
	public abstract void publish(CHART_ENTRY e1, CHART_ENTRY e2);
	public abstract ChartData2D getChartData();	    
	public abstract void webSocketUpdate(int index,CHART_ENTRY e);
	public abstract CHART_ENTRY clone(CHART_ENTRY e);
	public abstract void useAsDataSource();
    public void sort(Comparator<CHART_ENTRY> comparator){
    	Collections.sort(chartEntries, comparator);
    }
    public void sort(){
    	Collections.sort(chartEntries);
    	Collections.reverse(chartEntries);
    }
    
	public void reset(){
		chartEntries = new CopyOnWriteArrayList<>();
	}
	    
	@Override
	public void handle(Event<CHART_ENTRY> e) {
		// TODO Auto-generated method stub
		updateAndPublish(e.getData());
	}
	
	
}
