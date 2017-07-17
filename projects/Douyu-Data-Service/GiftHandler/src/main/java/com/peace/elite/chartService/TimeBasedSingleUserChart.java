package com.peace.elite.chartService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
import com.peace.elite.chartService.entity.ExtendedCharEntry2D;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.partition.Partition;
import com.peace.elite.partition.Partitions2D;
import com.peace.elite.redisRepository.GiftRepository;
import com.peace.elite.chartService.entity.BoundaryWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class TimeBasedSingleUserChart{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	 
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/time_based_single_user_chart";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/time_based_single_user_chart";
	
	private ChartData<ChartEntry2D, ChartEntry2D> chartData;
	
	public TimeBasedSingleUserChart() {
	}
	
	private String generateGroupName(Long money){
		return money/10+"元以下";
	}
	private ChartEntry2D generateBoundary(long amount){
		return new ChartEntry2D(amount, generateGroupName(amount), amount+"");
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
		Long[] range = {new Long(1500206400000l),Long.MAX_VALUE};//,new Long(1500141600000l)};
		Long length = 300000l;
		GivingDataSource givingDataSource = new GivingDataSource(range, giftRepository, receivingEventFactory);
		givingDataSource.filters.add((g)->"玩战争游戏".equals(g.getUserName()));
		Partitions2D<Giving, ChartEntry2D, BoundaryWrapper<Giving>> partitions = new Partitions2D<>( 
				//predicate
				GiftRepository.withInTimeInterval,
				//accumulate
				(giving, entry)->{
					Long[] timeInterval = timeToTimeInterval(giving.getTimeStamp(), new Long(1500206400000l),Long.MAX_VALUE, length);
					if(entry==null){
						return new ChartEntry2D(GiftRepository.getGiftPrice(giving.getGid()), GiftRepository.timeIntervalToLabel.apply(timeInterval), timeInterval[0]+"");
					}else{
						entry.increase(new Long(GiftRepository.getGiftPrice(giving.getGid())));
						return entry;
					}
				},
				//generateCriterion
				(giving)->{
					BoundaryWrapper<Giving> result = new BoundaryWrapper<Giving>();
					Long[] timeInterval = timeToTimeInterval(giving.getTimeStamp(), new Long(1500206400000l),Long.MAX_VALUE, length);
					result.setFirst(new Giving(1l, 1l, timeInterval[0], GiftRepository.timeIntervalToLabel.apply(timeInterval)));
					result.setSecond(new Giving(1l, 1l, timeInterval[1], GiftRepository.timeIntervalToLabel.apply(timeInterval)));
					return result;
				},
				//dataSource
				givingDataSource);
		
		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		Long start = new Date().getTime();
		givingDataSource.start();
		Long end1 = new Date().getTime();
		Long end2 = new Date().getTime();
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------\n\n\n");
		System.out.println("phase 1: "+ (end1-start)+" , each giving takes "+(end1-start)/132718);
		//System.out.println("phase 2: "+ (end2-end1));
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------");
		//webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/update", "dada");
	}
	
	
    @MessageMapping(WEB_SOCKET_APP_CHANNEL+"/init")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/init")
	public ChartData2D initRequestHandler(){
    	//init();
    	chartData.sort((e1, e2)->{
    		return e1.getId().compareTo(e2.getId());
    	});;
    	return chartData.getChartData();
	}
    
	@MessageMapping(WEB_SOCKET_APP_CHANNEL+"/sort")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/sort")
	public ChartData2D sortRequestHandler() {
		chartData.sort();
    	return chartData.getChartData();
	}
}
