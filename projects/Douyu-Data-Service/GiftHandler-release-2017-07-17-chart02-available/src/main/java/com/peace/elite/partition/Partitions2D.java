package com.peace.elite.partition;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Partitions2D<RAW_ENTRY,CHART_ENTRY,CRITERION extends Comparable<CRITERION>> extends EventFactory<CHART_ENTRY> implements Listener<RAW_ENTRY>{
	
	protected CopyOnWriteArrayList<Partition<RAW_ENTRY, CHART_ENTRY, CRITERION>> partitions = new CopyOnWriteArrayList<Partition<RAW_ENTRY, CHART_ENTRY, CRITERION>>();
	protected BiPredicate<RAW_ENTRY, CRITERION> predicate;
	protected BiFunction<RAW_ENTRY,CHART_ENTRY, CHART_ENTRY> accumulate;
	protected Function<RAW_ENTRY, CRITERION> generateCriterion;
	protected EventFactory<RAW_ENTRY> dataSource;
	
	public Partitions2D(BiPredicate<RAW_ENTRY, CRITERION> predicate, BiFunction<RAW_ENTRY,CHART_ENTRY, CHART_ENTRY> accumulate, Function<RAW_ENTRY, CRITERION> generateCriterion, EventFactory<RAW_ENTRY> dataSource){
		this.predicate = predicate;
		this.accumulate = accumulate;
		this.generateCriterion = generateCriterion;
		if(dataSource!=null){
			this.dataSource = dataSource;
			dataSource.register(this);
		}
	} 

	protected Partition<RAW_ENTRY, CHART_ENTRY, CRITERION> generatePartition(RAW_ENTRY raw){
		return new Partition<RAW_ENTRY, CHART_ENTRY, CRITERION> ((CHART_ENTRY)null,
				generateCriterion.apply(raw),
				predicate, 
				accumulate);
	}
	
	@Override
	public void handle(Event<RAW_ENTRY> e) {
		// TODO Auto-generated method stub
		for(Partition<RAW_ENTRY, CHART_ENTRY, CRITERION> p:partitions){
			//System.out.println(e.getData()+"   :   " +p.getCriterion());
			if(p.test(e.getData())){
				p.accumulate(e.getData());
				publish(new Event<>(p.getChartEntry()));
				return ;
			}
		}
	//	System.out.println(generateCriterion.apply(e.getData()));
		partitions.add(generatePartition(e.getData()));
		handle(e);
	}
}

