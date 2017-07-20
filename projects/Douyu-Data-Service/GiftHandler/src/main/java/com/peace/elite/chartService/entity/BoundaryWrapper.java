package com.peace.elite.chartService.entity;

import java.util.concurrent.ThreadLocalRandom;

import com.peace.elite.chartService.DailyMoneyBasedChart;
import com.peace.elite.partition.Partition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundaryWrapper<C extends Comparable<C>> implements Comparable<BoundaryWrapper<C>>{
	private C first;
	private C second;
	@Override
	public int compareTo(BoundaryWrapper<C> o) {
		// TODO Auto-generated method stub
		return first.compareTo(o.getFirst());
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o){
		boolean result = false;
		try{
			if(o == null || o instanceof BoundaryWrapper<?>){
				result = false;
			}else{
				result = first.equals(((BoundaryWrapper<C>)o).getFirst());
			}
		}catch(Exception e){
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(100));
			} catch (Exception ex) {
			}
			equals(o);
		}
		
		return result; 
	}
	@Override
	public int hashCode(){
		return first.hashCode();
	}
	
}