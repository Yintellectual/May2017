package com.peace.elite.chartService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.peace.elite.ChartData2D;
import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.chartService.chartData.ChartData;
import com.peace.elite.chartService.dataSource.GivingDataSource;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.partition.DynamicPartition2D;
import com.peace.elite.partition.Partitions2D;
import com.peace.elite.redisRepository.GiftRepository;

//@Controller
public class TimeBasedGivingsBarChartService extends ChartService2D<Giving>{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/generic";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/generic";
	
	private ChartData<ChartEntry2D> chartData;
	
	public TimeBasedGivingsBarChartService() {
	}
	
	private Long[] timeToTimeInterval(Long time, Long start, Long end, Long length){
		Long[] result = new Long[2];
		
		long delta = (time - start)%length;
		result[0] = time - delta;
		result[1] = Math.min(end, result[0]+length); 
		return result;
	}
	@PostConstruct
	public void init() {
		Long[] range = {new Long(1500120000000l),new Long(1500141600000l)};
		Long length = 300000l;
		GivingDataSource givingDataSource = new GivingDataSource(range, giftRepository, receivingEventFactory);
		Partitions2D<Long[]> partitions = new DynamicPartition2D<>(GiftRepository.withInTimeInterval, GiftRepository.sumOfPrices,
				//label generator
				GiftRepository.timeIntervalToLabel,  
				
				//id generator
				
				(interval)->""+interval[0], 
				//timebased partition generator
				(giving) -> {
					Long[] partition = timeToTimeInterval(giving.getTimeStamp(), range[0], range[1], length);
					return partition;
				}
				
				, givingDataSource);
		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		givingDataSource.start();
	}
	
    //@MessageMapping(WEB_SOCKET_APP_CHANNEL+"/init")
    //@SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/init")
	public ChartData2D initRequestHandler(){
    	chartData.sort((e1, e2)-> e1.getId().compareTo(e2.getId()));
    	return chartData.getChartData();
	}
    
	//@MessageMapping(WEB_SOCKET_APP_CHANNEL+"/sort")
    //@SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/sort")
	public ChartData2D sortRequestHandler() {
		chartData.sort();
    	return chartData.getChartData();
	}
}
