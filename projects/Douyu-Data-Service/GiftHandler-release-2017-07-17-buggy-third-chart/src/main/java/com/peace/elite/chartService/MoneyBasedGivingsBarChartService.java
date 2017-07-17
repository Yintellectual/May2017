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
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.partition.DynamicPartition2D;
import com.peace.elite.partition.Partitions2D;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class MoneyBasedGivingsBarChartService extends ChartService2D<Giving>{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/generic";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/generic";
	
	private ChartData<ChartEntry2D> chartData;
	
	public MoneyBasedGivingsBarChartService() {
	}
	
	private Giving generateMoneyPartition(Long money){
		return new Giving(money, money, 0l, money/10+"元以下");
	}
	

	@PostConstruct
	public void init() {
		Long[] range = {new Long(1500206400000l),Long.MAX_VALUE};//,new Long(1500141600000l)};
		GivingDataSource givingDataSource = new GivingDataSource(range, giftRepository, receivingEventFactory);
		Partitions2D<Giving> partitions1 = new DynamicPartition2D<>(GiftRepository.hasUid, GiftRepository.sumOfPrices,
				(giving) -> giving.getUserName(), (giving) -> "" + giving.getUid(), (giving) -> giving, givingDataSource);
		ChartData<ChartEntry2D> chartData1 = new ChartDataServiceFor2DimensionalCharts(partitions1, WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		chartData1.setWEB_SOCKET_PUBLISH_CHANNEL("1");
		Partitions2D<Giving[]> partitions = new DynamicPartition2D<>(
				//by 6,100,500 and individuals
				(g,t)->{
					long money = g.getGid();
					String name = g.getUserName();
					return (name.equals(t[1].getUserName()))
							||( (money>=t[0].getGid())
									&&
								(money<t[1].getGid()));
				}, 
				GiftRepository.sumOfGids,
				(t) -> t[1].getUserName(), 
				(t) -> "" + t[1].getUid(), 
				(giving) -> {
					Giving[] result = new Giving[2];
					long money = giving.getGid();
					
					if(money<100l){
						result[0] = generateMoneyPartition(0l);
						result[1] = generateMoneyPartition(100l);
					}else if(money<1500l){
						result[0] = generateMoneyPartition(100l);
						result[1] = generateMoneyPartition(1500l);
					}else if(money<4000l){
						result[0] = generateMoneyPartition(1500l);
						result[1] = generateMoneyPartition(4000l);
					}else{
						result[0] = giving;
						result[1] = giving;
					}					
					return result;
				}, 
				(EventFactory<Giving>) chartData1);

		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		Long start = new Date().getTime();
		givingDataSource.start();
		Long end1 = new Date().getTime();
		//chartData1.useAsDataSource();
		Long end2 = new Date().getTime();
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------\n\n\n");
		System.out.println("phase 1: "+ (end1-start)+" , each giving takes "+(end1-start)/132718);
		//System.out.println("phase 2: "+ (end2-end1));
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------");
	}
	
    @MessageMapping(WEB_SOCKET_APP_CHANNEL+"/init")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/init")
	public ChartData2D initRequestHandler(){
    	chartData.sort();
    	return chartData.getChartData();
	}
    
	@MessageMapping(WEB_SOCKET_APP_CHANNEL+"/sort")
    @SendTo(WEB_SOCKET_PUBLISH_CHANNEL+"/sort")
	public ChartData2D sortRequestHandler() {
		chartData.sort();
    	return chartData.getChartData();
	}



}
