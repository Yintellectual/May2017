package com.peace.elite.partition;

import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.chartService.dataSource.GivingDataSource;
import com.peace.elite.entities.Giving;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;

public class DynamicPartition2D<T> extends Partitions2D<T>{


	private Function<Giving, T> partitionGenerator;

	public DynamicPartition2D(BiPredicate<Giving, T> predicate, Function<Iterable<Giving>, Long> reduce, Function<T, String> labelGeneration, Function<T, String> idGenerator, Function<Giving, T> partitionGenerator,  EventFactory<Giving>  dataSource){
		super(predicate, reduce, labelGeneration, idGenerator, dataSource);	
		this.partitionGenerator = partitionGenerator;
	}
	@Override
	public void partition(Giving g) {
		// TODO Auto-generated method stub
		boolean needNewPartition = true;
		for (T partition : partitions.keySet()) {
			if (partitions.get(partition).test(g, partition)) {
				needNewPartition = false;
			}
		}
		if (needNewPartition) {
			addPartition(partitionGenerator.apply(g));
		}
		super.partition(g);
	}
}
