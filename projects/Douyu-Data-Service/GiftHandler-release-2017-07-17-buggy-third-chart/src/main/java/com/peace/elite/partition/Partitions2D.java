package com.peace.elite.partition;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.chartService.dataSource.GivingDataSource;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//never holds the data

//usage:
//	1. construct
//  2. addPartitions
//	3. partition givings
@Data
@NoArgsConstructor
public class Partitions2D<T> extends EventFactory<ChartEntry2D> implements Listener<Giving>{
	
	protected BiPredicate<Giving, T> predicate;
	protected Function<Giving, Long> getValue;
	protected ConcurrentMap<T, GivingPartition<T>> partitions = new ConcurrentHashMap<>();
	protected Function<T, String> labelGeneration;
	protected Function<T, String> idGenerator;
	protected EventFactory<Giving> dataSource;
	
	public Partitions2D(BiPredicate<Giving, T> predicate, Function<Giving, Long> getValue, Function<T, String> labelGeneration, Function<T, String> idGenerator,EventFactory<Giving> dataSource ){
		this.predicate = predicate;
		this.getValue = getValue;
		this.labelGeneration = labelGeneration;
		this.idGenerator = idGenerator;
		this.dataSource = dataSource;
		dataSource.register(this);
	} 
	
	protected GivingPartition<T> newPartition(T partition) {
		GivingPartition<T> result = new GivingPartition<T>(predicate, getValue, labelGeneration, idGenerator, partition);
		return result;
	}

	public void addPartition(T partition) {
		if (!partitions.keySet().contains(partition)) {
			partitions.put(partition, newPartition(partition));
		}
	}

	public void addPartitions(List<T> partitionList) {
		for (T partition : partitionList) {
			addPartition(partition);
		}
	}

	public void remove(T partition) {
		partitions.remove(partition);
	}

	public void partition(Giving giving) {
		// TODO Auto-generated method stub
		for (T partition : partitions.keySet()) {
			if (partitions.get(partition).test(giving, partition)) {
				sumUpAndPublis(giving, partition);
				break;
			}
		}
	}
	
	public void sumUpAndPublis(Giving giving, T partition){
		GivingPartition<T> p = partitions.get(partition);
		p.sumUp(giving);
		publish(new Event<ChartEntry2D>(p));
	}
	public void sumUpAndPublis(Giving giving, GivingPartition<T> p ){
		p.sumUp(giving);
		publish(new Event<ChartEntry2D>(p));
	}
	public ArrayList<ChartEntry2D> getData() {
		ArrayList<ChartEntry2D> result = new ArrayList<ChartEntry2D>();

		for (GivingPartition<T> partition : partitions.values()) {
			result.add(partition);
		}
		return result;
	}

	@Override
	public void handle(Event<Giving> e) {
		// TODO Auto-generated method stub

		partition(e.getData());
	}
}

