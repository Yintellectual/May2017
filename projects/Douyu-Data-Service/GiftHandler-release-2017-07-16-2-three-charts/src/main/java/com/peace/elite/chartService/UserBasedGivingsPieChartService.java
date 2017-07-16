package com.peace.elite.chartService;

import java.util.ArrayList;
import java.util.Collections;

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
public class UserBasedGivingsPieChartService extends ChartService2D<Giving>{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/generic";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/generic";
	
	private ChartData<ChartEntry2D> chartData;
	
	public UserBasedGivingsPieChartService() {
	}
	@PostConstruct
	public void init() {
		GivingDataSource givingDataSource = new GivingDataSource(new Long[]{new Long(1500000452706l),Long.MAX_VALUE}, giftRepository, receivingEventFactory);
		Partitions2D<Giving> partitions = new DynamicPartition2D<>(GiftRepository.hasUid, GiftRepository.sumOfPrices,
				(giving) -> giving.getUserName(), (giving) -> "" + giving.getUid(), (giving) -> giving, givingDataSource);
		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		givingDataSource.start();
	}
	
   // @MessageMapping(WEB_SOCKET_APP_CHANNEL+"/init")
   // @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/init")
	public ChartData2D initRequestHandler(){
    	return chartData.getChartData();
	}
    
	//@MessageMapping(WEB_SOCKET_APP_CHANNEL+"/sort")
    //@SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/sort")
	public ChartData2D sortRequestHandler() {
		chartData.sort();
    	return chartData.getChartData();
	}
}
