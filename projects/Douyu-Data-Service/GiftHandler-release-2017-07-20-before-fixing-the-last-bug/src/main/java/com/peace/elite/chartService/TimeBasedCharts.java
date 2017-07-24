package com.peace.elite.chartService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
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
import com.peace.elite.chartService.entity.ExtendedCharEntry2D;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.partition.Partition;
import com.peace.elite.partition.Partitions2D;
import com.peace.elite.redisRepository.GiftRepository;
import com.peace.elite.chartService.entity.BoundaryWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@Controller
public class TimeBasedCharts {//implements Listener<BoundaryWrapper<Long>>{

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private ReceivingEventFactory receivingEventFactory;
	@Autowired
	private RecordService recordService;
//	@Autowired
//	private Clocker clocker;
	GivingDataSource givingDataSource;
	private final String WEB_SOCKET_APP_CHANNEL = "/2-dimensional/day_based_single_user_chart";
	private final String WEB_SOCKET_PUBLISH_CHANNEL = "/topic/2-dimensional/day_based_single_user_chart";
	private List<Predicate<TypedTuple<String>>> filters = new ArrayList<>();
	private List<Predicate<Giving>> filters2 = new ArrayList<>();
	private ChartData<ChartEntry2D, ChartEntry2D> chartData;
	private Partitions2D<Giving, ChartEntry2D, BoundaryWrapper<Long>> partitions;
	// ,new

	public TimeBasedCharts() {
	}

	private String generateGroupName(Long money) {
		return money / 10 + "元以下";
	}

	private ChartEntry2D generateBoundary(long amount) {
		return new ChartEntry2D(amount, generateGroupName(amount), amount + "");
	}

	private Long[] timeToTimeInterval(Long time, Long start, Long end, Long length) {
		Long[] result = new Long[2];

		long delta = (time - start) % length;
		result[0] = time - delta;
		result[1] = Math.min(end, result[0] + length);
		return result;
	}

	@PostConstruct
	public void init() {
		//clocker.register(this);
		Long length = 86400000l ;
		Long range[] = new Long[2];
//		long s = clocker.getRange()[0];
		range[0] = 1500141600000l - length -length;
		range[1] = Long.MAX_VALUE;
				
		givingDataSource = new GivingDataSource(range, giftRepository, receivingEventFactory);
		givingDataSource.filters.addAll(filters);
		givingDataSource.filters2.addAll(filters2);
		recordService.attach(givingDataSource);
		partitions = new Partitions2D<>(
				// predicate
				GiftRepository.withInTimeInterval,
				// accumulate

				(giving, entry) -> {
					Long[] timeInterval = timeToTimeInterval(giving.getTimeStamp(), range[0],
							Long.MAX_VALUE, length);
					entry.increase(new Long(GiftRepository.getGiftPrice(giving.getGid())));
					entry.setLabel(GiftRepository.timeIntervalToDateLabel.apply(timeInterval));
					entry.setId(timeInterval[0]+"");
					return entry;
				},
				// generateCriterion
				(giving) ->

				{
					BoundaryWrapper<Long> result = new BoundaryWrapper<Long>();
					Long[] timeInterval = timeToTimeInterval(giving.getTimeStamp(), range[0], Long.MAX_VALUE, length);
					result.setFirst(timeInterval[0]);
					result.setSecond(timeInterval[1] - 1);
					return result;
				},
				// dataSource
				givingDataSource);
		chartData = new ChartDataServiceFor2DimensionalCharts(partitions, "1", webSocket);
		Long start = new Date().getTime();
		System.out.println("starting");
		givingDataSource.start();
		partitions.cleanTailData();
		chartData.setWEB_SOCKET_PUBLISH_CHANNEL(WEB_SOCKET_PUBLISH_CHANNEL);
		Long end1 = new Date().getTime();
		Long end2 = new Date().getTime();
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------\n\n\n");
		System.out.println("phase 1: " + (end1 - start) + " , each giving takes " + (end1 - start) / 132718);
		// System.out.println("phase 2: "+ (end2-end1));
		System.out.println("----------------------------------------------------------");
		System.out.println("----------------------------------------------------------");
		// webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL+"/update",
		// "dada");
	}

	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/init")
	@SendTo(WEB_SOCKET_PUBLISH_CHANNEL + "/init")
	public ChartData2D initRequestHandler(@Payload String userName) {
		recordService.sendAll();
		return chartData.getChartData();
	}

	private void addFilter(String str) {
		filters.add((t) -> {
			String s = "";
			s = ":" + str + ":";
			return t.getValue().contains(s);
		});
	}

	private void addNotFilter(String str) {
		filters.add((t) -> {
			String s = "";
			s = ":" + str + ":";
			return !t.getValue().contains(s);
		});
	}

	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/update2")
	public void update2RequestHandler(@Payload String userName) {
		System.out.println("\n\n\n\n\nPreparing Data for " + userName);
		partitions = null;
		chartData = null;
		receivingEventFactory.remove(givingDataSource);
		givingDataSource = null;
		filters.clear();
		filters2.clear();

		if (userName.equals("global")) {
			filters.add((e) -> true);
			filters2.add((e) -> true);
	
		} 
		else if (userName.contains("元以下")) {
			Pattern pp = Pattern.compile(".*(\\d*).*");
			Matcher mm = pp.matcher(userName);
			if (mm.matches()) {
				addNotFilter(196 + "");
				addNotFilter(195 + "");
				addNotFilter(997 + "");
				addNotFilter(988 + "");
			}
			filters2.add((g) -> g.getGid() != 196);
			filters2.add((g) -> g.getGid() != 195);
			filters2.add((g) -> g.getGid() != 988);
			filters2.add((g) -> g.getGid() != 997);
			
		} else {
			String keyUser = giftRepository.getKeyUserByName(userName);
			Pattern p = Pattern.compile(".*:(\\d*).*");
			Matcher m = p.matcher(keyUser);
			String uid = "";
			if (m.matches()) {
				uid = m.group(1);
			}
			addFilter(uid);
			filters2.add((g) -> userName.equals(g.getUserName()));
			
		}
		init();
		// givingDataSource.filters2.clear();

		// System.out.println("Clear");

		// System.out.println("Filter Ready");
		// givingDataSource.start();
		// System.out.println("working...");
		recordService.sendAll();
		System.out.println("sorted");
		webSocket.convertAndSend(WEB_SOCKET_PUBLISH_CHANNEL + "/update2", chartData.getChartData());
		System.out.println(chartData.getChartData().getLabels());
		System.out.println(chartData.getChartData().getData());
		chartData.setWEB_SOCKET_PUBLISH_CHANNEL(WEB_SOCKET_PUBLISH_CHANNEL);
		System.out.println("Resume real time data feeding");
	}

	@MessageMapping(WEB_SOCKET_APP_CHANNEL + "/sort")
	@SendTo(WEB_SOCKET_PUBLISH_CHANNEL + "/sort")
	public ChartData2D sortRequestHandler() {
		
		return chartData.getChartData();
	}

//	@Override
//	public void handle(Event<BoundaryWrapper<Long>> e) {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public void handle(Event<BoundaryWrapper<Long>> e) {
//		// TODO Auto-generated method stub
//		update2RequestHandler("global");
//	}
}
