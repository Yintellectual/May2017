package com.peace.elite.partition;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.redisRepository.GiftRepository;

import lombok.Data;

@Data
public class GivingPartition<T> extends ChartEntry2D implements BiPredicate<Giving, T> {

	@Autowired
	GiftRepository giftRepository;

	private T partition;
	private CopyOnWriteArrayList<Giving> elements = new CopyOnWriteArrayList<>();
	private BiPredicate<Giving, T> predicate;
	private Function<Iterable<Giving>, Long> reduce;
	private Function<T, String> labelGenerator;
	private Function<T, String> idGenerator;
	public GivingPartition(BiPredicate<Giving, T> predicate, Function<Iterable<Giving>, Long> reduce, Function<T, String> labelGenerator, Function<T, String> idGenerator,  T partition) {
		this.predicate = predicate;
		this.reduce = reduce;
		this.labelGenerator = labelGenerator;
		this.idGenerator = idGenerator;
		this.partition = partition;
		this.label = labelGenerator.apply(partition);
		this.id = idGenerator.apply(partition);
	}

	@Override
	public boolean test(Giving giving, T partition) {
		if (predicate.test(giving, partition)) {
			elements.add(giving);
			data = reduce.apply(elements);
			return true;
		}
		return false;
	}
}
//
//class TimePartitions {
//	@Autowired
//	GiftRepository giftRepository;
//
//	protected GivingPartition newPartition(Long[] timeInterval) {
//
//		GivingPartition result = new GivingPartition(
//				giftRepository.withInTimeInterval,
//				giftRepository.sumOfPrices);
//
//		result.setLabel(giftRepository.timeIntervalToLabel.apply(timeInterval));
//		return result;
//	}
//
//}

