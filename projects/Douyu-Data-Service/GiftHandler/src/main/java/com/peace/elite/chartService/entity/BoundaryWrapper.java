package com.peace.elite.chartService.entity;

import com.peace.elite.chartService.MoneyBasedGivingsBarChartService;

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
}