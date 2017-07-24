package com.peace.elite.chartService.dataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.peace.elite.GiftHandlerApplication.ReceivingEventFactory;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.redisRepository.GiftRepository;

public class GivingDataSource extends EventFactory<Giving> implements Listener<Giving>{

	public List<Predicate<TypedTuple<String>>> filters = new ArrayList<Predicate<TypedTuple<String>>>();
	public List<Predicate<Giving>> filters2 = new ArrayList<Predicate<Giving>>();
	private Long[] timeInterval;
	private GiftRepository giftRepository;
	private ReceivingEventFactory receivingEventFactory;
	
	public GivingDataSource(Long[] timeInterval, GiftRepository giftRepository, ReceivingEventFactory receivingEventFactory) {
		// TODO Auto-generated constructor stub
		this.giftRepository = giftRepository;
		this.timeInterval = timeInterval;
		this.receivingEventFactory = receivingEventFactory;
	}
	public void start(){
		Predicate<TypedTuple<String>> filter = (g)->true;
		for(Predicate<TypedTuple<String>> f:filters){
			filter = filter.and(f);
		}
		
		Set<TypedTuple<String>> tuples = giftRepository.givingsTimeOriented(timeInterval[0], timeInterval[1]);
		System.out.println("~~~~~data ready!");
		//if(tuples.size()>1000){
			tuples.parallelStream().filter(filter).map(tuple->giftRepository.recoverGiving(tuple)).map(g->new Event<Giving>(g)).forEach(e->publish(e));
		//}else{
			//tuples.stream().filter(filter).map(tuple->giftRepository.recoverGiving(tuple)).map(g->new Event<Giving>(g)).forEach(e->publish(e));
		//}
		if(timeInterval[1]==Long.MAX_VALUE){
			receivingEventFactory.register(this);
		}
	}
	@Override
	public void handle(Event<Giving> e) {
		// TODO Auto-generated method stub
		Predicate<Giving> filter = (g)->true;
		for(Predicate<Giving> f:filters2){
			filter = filter.and(f);
		}
		if(filter.test(e.getData())){
			publish(e);
		}
	}
	
//	//keyGiving:givingCounter
//	public String retrieveKey(String str){
//		Pattern pattern = Pattern.compile("(.*?:\\d*:\\d*):.*");
//		Matcher matcher = pattern.matcher(str);
//		if(matcher.matches()){
//			return matcher.group(1);
//		}
//		return null;
//	}
	
//	public Giving recoverGiving(TypedTuple<String> tuple){
//		Giving result = null;
//		Long timestamp = new Double(tuple.getScore()).longValue();
//		String value = tuple.getValue();
//		String keyGiving = retrieveKey(value);
//		if(keyGiving == null){
//			System.out.println("!!!!!!!!!!!!Illegal Giving Record"+tuple);
//			throw new RuntimeException();
//		}else{
//			long uid = giftRepository.getGivingUid(keyGiving);
//			
//			long gid = giftRepository.getGivingGid(keyGiving);
//			String name = giftRepository.getUserName(uid);
//			result = new Giving(uid, gid, timestamp, name);
//		}
//		
//		return result;
//	}
}
