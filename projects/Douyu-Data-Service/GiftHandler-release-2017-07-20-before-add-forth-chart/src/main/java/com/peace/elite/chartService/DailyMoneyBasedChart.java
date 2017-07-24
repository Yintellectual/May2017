package com.peace.elite.chartService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.peace.elite.ChartData2D;
import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.GiftHandlerApplication.Clocker;
import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.chartService.chartData.ChartData;
import com.peace.elite.chartService.dataSource.GivingDataSource;
import com.peace.elite.chartService.entity.BoundaryWrapper;
import com.peace.elite.chartService.entity.ExtendedCharEntry2D;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.partition.Partition;
import com.peace.elite.partition.Partitions2D;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class DailyMoneyBasedChart implements Listener<BoundaryWrapper<Long>>{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	@Autowired
	private Clocker clocker;
	private Long[] range ;//= new Long[]{clocker.getRange()[0],Long.MAX_VALUE};
	
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/generic";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/generic";

	private ChartData<ChartEntry2D, ChartEntry2D> chartData;

	public DailyMoneyBasedChart() {
	}

	private String generateGroupName(Long money) {
		return money / 10 + "元以下";
	}

	private ChartEntry2D generateBoundary(long amount) {
		return new ChartEntry2D(amount, generateGroupName(amount), amount + "");
	}

	//@PostConstruct
	public void init() {
		GivingDataSource givingDataSource = new GivingDataSource(range, giftRepository, receivingEventFactory);
		Partitions2D<Giving, ChartEntry2D, Giving> partitions1 = new Partitions2D<>(
				// predicate
				GiftRepository.hasUid,
				// accumulate
				(giving, entry) -> {
					entry.increase(GiftRepository.getGiftPrice(giving.getGid()));
					entry.setLabel(giving.getUserName() == null ? "unknown" : giving.getUserName());
					entry.setId(giving.getUid() + "");
					return entry;
				},
				// generateCriterion
				(giving) -> giving,
				// dataSource
				givingDataSource);

		ChartData<ChartEntry2D, ChartEntry2D> chartData1 = new ChartDataServiceFor2DimensionalCharts(partitions1,
				WEB_SOCKET_PUBLISH_CHANNEL, webSocket);
		chartData1.setWEB_SOCKET_PUBLISH_CHANNEL("1");
		Partitions2D<ChartEntry2D, ChartEntry2D, BoundaryWrapper<ChartEntry2D>> partitions = new Partitions2D<>(
				// predicate
				(e, b) -> {
					long money = e.getData();
					if (money < 0) {
						money *= -1;
					}
					String name = e.getLabel();

					boolean result = false;
					result = (name.equals(b.getSecond().getLabel()))
							|| ((money < b.getSecond().getData()) && (money >= b.getFirst().getData()));
					return result;
				},
				// accumulate
				(e, entry) -> {
					long money = e.getData();
					String name = e.getLabel();
					String id = e.getId();
					if (money < 0) {
						// error
					}
					if (money < 100) {
						name = generateGroupName(100l);
						id = 100 + "";
					} else if (money < 1500) {
						name = generateGroupName(1500l);
						id = 1500 + "";
					} else if (money < 4000) {
						name = generateGroupName(4000l);
						id = 4000 + "";
					} else {

					}
					entry.increase(money);
					entry.setLabel(name);
					entry.setId(id);
					return entry;

				},
				// generateCriterion
				(entry) -> {
					BoundaryWrapper<ChartEntry2D> result = new BoundaryWrapper<ChartEntry2D>();
					long money = entry.getData();

					if (money < 0) {
						// do nothing
					}
					if (money < 100l) {
						result.setFirst(generateBoundary(0l));
						result.setSecond(generateBoundary(100l));
					} else if (money < 1500l) {
						result.setFirst(generateBoundary(100l));
						result.setSecond(generateBoundary(1500l));
					} else if (money < 4000l) {
						result.setFirst(generateBoundary(1500l));
						result.setSecond(generateBoundary(4000l));
					} else {
						result.setFirst(entry);
						result.setSecond(entry);
					}
					return result;
				},
				// dataSource
				null

		);

		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, "1", webSocket);
		Long start = new Date().getTime();
		givingDataSource.start();
		partitions1.cleanTailData();
		Long end1 = new Date().getTime();
		partitions.setDataSource(chartData1);
		chartData1.register(partitions);
		chartData1.useAsDataSource();
		partitions.cleanTailData();
		Long end2 = new Date().getTime();
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------\n\n\n");
		System.out.println("phase 1: " + (end1 - start) + " , each giving takes " + (end1 - start) / 132718);
		// System.out.println("phase 2: "+ (end2-end1));
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------");
//		Thread thread = new Thread(() -> {
//			while (true) {
//				try {
//					Thread.sleep(6000);
//				} catch (Exception e) {
//				}
//				for (Partition<ChartEntry2D, BoundaryWrapper<ChartEntry2D>> p : partitions.getPartitions()) {
//					if (p.getChartEntry() != null) {
//						p.getChartEntry().setData(0l);
//					}
//				}
//				chartData1.useAsDataSource();
//				webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL + "/update", chartData.getChartDataCustomOrder((e1, e2) -> (int) (e1.getData() - e2.getData())));
//			}
//		});
//		thread.start();

	}
	private Long[] timeToTimeInterval(Long time, Long start, Long end, Long length) {
		Long[] result = new Long[2];

		long delta = (time - start) % length;
		result[0] = time - delta;
		result[1] = Math.min(end, result[0] + length);
		return result;
	}
	private Long dateRangeToLong(String dateRange){
		String date = dateRange.split("-")[0];
		String[] s = date.split("\\.");
		Long result = 0l;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(s[0])-1, Integer.parseInt(s[1]), 20, 0, 0);
			result = calendar.getTime().getTime();
		}catch(Exception e){}
		return result;
	}
	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/update")
	public void historicalChart(@Payload String dateRange){
		
		
		Long date = dateRangeToLong(dateRange);
		range = timeToTimeInterval(date, 1500202800000l, Long.MAX_VALUE, 86400000l);
		if(range[1]>new Date().getTime()){
			range[1] = Long.MAX_VALUE;
		}
		init();
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL + "/update", chartData.getChartDataCustomOrder((e1, e2) -> (int) (e1.getData() - e2.getData())));
	}
	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/init")
	@SendTo(WEB_SOCKET_PUBLISH_CHANNEL + "/init")
	public ChartData2D initRequestHandler() {
		range = new Long[]{clocker.getRange()[0],Long.MAX_VALUE};
		init();
		return chartData.getChartDataCustomOrder((e1, e2) -> (int) (e1.getData() - e2.getData()));
	}

	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/sort")
	@SendTo(WEB_SOCKET_PUBLISH_CHANNEL + "/sort")
	public ChartData2D sortRequestHandler() {
		
		return chartData.getChartDataCustomOrder((e1, e2) -> (int) (e1.getData() - e2.getData()));
	}

	@Override
	public void handle(Event<BoundaryWrapper<Long>> e) {
		// TODO Auto-generated method stub
		chartData = null;
		init();
	}
}
