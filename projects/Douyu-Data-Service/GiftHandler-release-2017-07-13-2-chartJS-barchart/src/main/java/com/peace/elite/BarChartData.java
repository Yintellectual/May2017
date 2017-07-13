package com.peace.elite;

import lombok.Data;

@Data
public class BarChartData{
	public BarChartData(){}
	public BarChartData(String[] labels, Long[] data){
		this.labels = labels;
		this.data = data;
	}
	String[] labels;
	Long[] data;
}