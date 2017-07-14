package com.peace.elite.functionals;

import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import com.peace.elite.ChartDataServiceFor2DimensionalCharts;
import com.peace.elite.entities.Giving;
import com.peace.elite.entities.SmallGift;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.Listener;

public class DynamicPartition2D<T> extends Partitions2D<T> implements Listener<SmallGift>{


	private Function<Giving, T> partitionGenerator;

	public DynamicPartition2D(BiPredicate<Giving, T> predicate, Function<List<Giving>, Long> reduce, Function<T, String> labelGeneration, Function<T, String> idGenerator, Function<Giving, T> partitionGenerator){
			this.predicate = predicate;
			this.reduce = reduce;
			this.labelGeneration = labelGeneration;
			this.idGenerator = idGenerator;
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

	@Override
	public void handle(Event<SmallGift> e) {
		// TODO Auto-generated method stub
		partition(new Giving(e.getData().getUid(), e.getData().getGfid(), new Date().getTime(), e.getData().getNn()));
	}
}
