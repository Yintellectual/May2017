package com.peace.elite.partition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.peace.elite.chartService.entity.BoundaryWrapper;
import com.peace.elite.entities.ChartEntry2D;
import com.peace.elite.entities.Giving;
import com.peace.elite.eventListener.Event;
import com.peace.elite.eventListener.EventFactory;
import com.peace.elite.eventListener.Listener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Partitions2D<RAW_ENTRY extends Comparable<RAW_ENTRY>, CHART_ENTRY extends Comparable<CHART_ENTRY>, CRITERION extends Comparable<CRITERION>>
		extends EventFactory<ChartEntry2D> implements Listener<RAW_ENTRY> {

	protected ConcurrentSkipListSet<Partition<RAW_ENTRY, CRITERION>> partitions = new ConcurrentSkipListSet<Partition<RAW_ENTRY, CRITERION>>();
	protected BiPredicate<RAW_ENTRY, CRITERION> predicate;
	protected BiFunction<RAW_ENTRY, ChartEntry2D, ChartEntry2D> accumulate;
	protected Function<RAW_ENTRY, CRITERION> generateCriterion;
	protected EventFactory<RAW_ENTRY> dataSource;
	protected CopyOnWriteArrayList<Event<RAW_ENTRY>> tempEntries = new CopyOnWriteArrayList<>();

	public Partitions2D(BiPredicate<RAW_ENTRY, CRITERION> predicate,
			BiFunction<RAW_ENTRY, ChartEntry2D, ChartEntry2D> accumulate,
			Function<RAW_ENTRY, CRITERION> generateCriterion, EventFactory<RAW_ENTRY> dataSource) {
		this.predicate = predicate;
		this.accumulate = accumulate;
		this.generateCriterion = generateCriterion;
		if (dataSource != null) {
			this.dataSource = dataSource;
			dataSource.register(this);
		}
	}

	protected Partition<RAW_ENTRY, CRITERION> generatePartition(RAW_ENTRY raw) {
		return new Partition<RAW_ENTRY, CRITERION>(new ChartEntry2D(), generateCriterion.apply(raw), predicate,
				accumulate);
	}

	@Override
	public void handle(Event<RAW_ENTRY> e) {
		// TODO Auto-generated method stub
		for (Partition<RAW_ENTRY, CRITERION> p : partitions) {
			// System.out.println(e.getData()+" : " +p.getCriterion());

			if (p.test(e.getData())) {
				synchronized (p) {
					publish(new Event<>(p.getChartEntry().minusClone()));
					p.accumulate(e.getData());
					publish(new Event<>(p.getChartEntry().clone()));
					return;
				}
			}

		}
		Partition<RAW_ENTRY, CRITERION> p = generatePartition(e.getData());
		// try{
		// partitions.add(p);
		// }catch(Exception ex){
		// System.out.println("~~~~~~~~~~~~~~~!!!!!!!!!!!!!!"+partitions.size());
		// System.out.println(e.getData());
		// ex.printStackTrace(System.out);
		p.accumulate(e.getData());
		partitions.add(p);
		publish(new Event<>(p.getChartEntry()));
		// System.out.println("~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!"+partitions.size());
		// tempEntries.add(e);
		// }

		// System.out.println(generateCriterion.apply(e.getData()));

		// handle(e);
	}

	public void cleanTailData() {
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+partitions.size());
		CopyOnWriteArrayList<Event<RAW_ENTRY>> tempEntries1 = tempEntries;
		// ConcurrentSkipListSet<Partition<RAW_ENTRY,CHART_ENTRY,CRITERION>>
		// temp1 = temp;
		// try{

		// temp = new ConcurrentSkipListSet<>();

		tempEntries = new CopyOnWriteArrayList<>();
		System.out.println(tempEntries1.size());
		// temp1.stream().forEach((e)->partitions.add(e));
		if (tempEntries1.size() > 1000) {
			tempEntries1.parallelStream().forEach((e) -> handle(e));
		} else {
			tempEntries1.stream().forEach((e) -> handle(e));
		}
		// }catch(Exception e){
		// e.printStackTrace(System.out);
		// cleanTailData();
		// }
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+partitions.size());

		// List<String> print = partitions.stream().sorted().filter(p ->
		// p.getChartEntry() == null)
		// .map(p -> p.getCriterion() + ": " +
		// p.getChartEntry()).collect(Collectors.toList());
		// if (print != null && print.size() != 0) {
		// partitions.stream().sorted().map(p -> p.getCriterion() + ": " +
		// p.getChartEntry())
		// .forEach(System.out::println);
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// print.forEach(System.out::println);
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// tempEntries1.forEach(System.out::println);
		// }
		// partitions.stream().map(p->p.getChartEntry()).forEach(System.out::println);
		// partitions.stream().map(p->p.getCriterion()).forEach(System.out::println);
		if (tempEntries.size() != 0) {
			// System.out.println("==================================");
			cleanTailData();
		}
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+partitions.size());
	}
}
