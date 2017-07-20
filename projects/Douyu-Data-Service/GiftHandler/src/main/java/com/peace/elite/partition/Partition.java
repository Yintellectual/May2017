package com.peace.elite.partition;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.peace.elite.entities.ChartEntry2D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partition<RAW_ENTRY, CRITERION extends Comparable<CRITERION>> implements Predicate<RAW_ENTRY>, Comparable<Partition<RAW_ENTRY, CRITERION>> {

	private ChartEntry2D chartEntry = new ChartEntry2D();
	private CRITERION criterion;
	private BiPredicate<RAW_ENTRY, CRITERION> predicate;
	private BiFunction<RAW_ENTRY, ChartEntry2D, ChartEntry2D> accumulate;

	@Override
	public boolean test(RAW_ENTRY raw) {
		return predicate.test(raw, criterion);
	}

	public void accumulate(RAW_ENTRY raw) {
		chartEntry = accumulate.apply(raw, chartEntry);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o){
		try{
			if(o == null || o instanceof Partition){
		
			return false;
			}
		}catch(Exception e){
			return false;
		}
		
		return criterion.equals(((Partition<RAW_ENTRY, CRITERION>)o).getCriterion());
	}
	
	@Override
	public int hashCode(){
		return criterion.hashCode();
	}

	@Override
	public int compareTo(Partition<RAW_ENTRY, CRITERION> o) {
		// TODO Auto-generated method stub
		return criterion.compareTo(o.criterion);
	}
}
