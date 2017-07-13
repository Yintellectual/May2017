package com.peace.elite;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataMinor implements Runnable {

	private long startTime;
	// -1 means to real time
	private long endTime;
	// can be null
	//private String[] includedUsers;
	// can be null
	//private String[] excludedUsers;
	// can be null
	//private Long[] includedGid;
	// can be null
	//private Long[] excludedGid;
	//private boolean sort;
	// -1 changes to userOrientedMod
	private long timeUnit;
	private GiftRepository giftRepository;
	private ChartDataServiceFor2DimensionalCharts chartService;
	
	private boolean enbleUserOrientedUpdate = false;
	private boolean enbleTimeOrientedUpdate = false;
	
	public DataMinor(long startTime, long endTime, long timeUnit, GiftRepository giftRepository, ChartDataServiceFor2DimensionalCharts chartService){
	
		this.startTime = startTime;
		this.endTime = endTime;
		//this.sort = sort;
		this.timeUnit = timeUnit;
		this.giftRepository = giftRepository;
		this.chartService = chartService;
		
	}
	@Override
	public void run() {
		if (timeUnit == -1l) {
			userOrientedMining();
			if (endTime == -1) {
				enbleUserOrientedUpdate = true;
			}
		} else {
			timeOrientedMining();
			if (endTime == -1) {
				enbleTimeOrientedUpdate = true;
			}
		}
		System.out.println("done");
	}

	public void userOrientedMining() {
	
		Set<TypedTuple<String>> givings = giftRepository.givingsTimeOriented(startTime, endTime);
		chartService.setData(new ArrayList<>());
		givings.stream().map(giving->giving.getValue()).map(givingKey->{
			long gid = retrieveGid(givingKey);
			long price = giftRepository.getGiftPrice(gid);
			long uid = retrieveUid(givingKey);
			String name = giftRepository.getUserName(uid);
			return new ChartEntry2D(price, name, uid+"");
		}).forEach(e->chartService.update(e));
	}

	
	public Long retrieveUid(String givingKey){
		Pattern uidPattern = Pattern.compile(".*?:(\\d*):\\d*.*");
		return retrieveLong(givingKey, uidPattern);
	}
	public Long retrieveGid(String givingKey){
		Pattern gidPattern = Pattern.compile(".*?:\\d*:(\\d*).*");
		return retrieveLong(givingKey, gidPattern);
	}
	private Long retrieveLong(String string, Pattern pattern){
		Matcher matcher = pattern.matcher(string);
		if(matcher.matches()){
			return Long.parseLong(matcher.group(1));
		}
		return -1l;
	}
	public String timeLabel(long timeStamp){
		Date localStartDate = new Date(timeStamp);
		DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
		String timeLabel = format.format(localStartDate);
		return timeLabel;
	}
	
	public long giftPriceSum(Set<TypedTuple<String>> givings){
		return givings.stream().map(giving->giving.getValue()).mapToLong(givingKey->retrieveGid(givingKey)).filter(gid->gid!=-1l)
				.map(gid->giftRepository.getGiftPrice(gid))
				.reduce(0, (l1, l2)->l1+l2);
	}
	
	public void timeOrientedMining() {
		java.util.ArrayList<ChartEntry2D> data = new ArrayList<>();
		
		for(long startTime = this.startTime;startTime<endTime;startTime+=timeUnit){
			long s = startTime;
			long e = startTime+timeUnit;
			if(e>endTime){
				e=endTime;
			}
			Set<TypedTuple<String>> givings = giftRepository.givingsTimeOriented(s, e);
			long giftPriceSum = giftPriceSum(givings);
			String label =timeLabel(s);
			data.add(new ChartEntry2D(giftPriceSum, label, label));
		}
		chartService.setData(data);
	}
}
