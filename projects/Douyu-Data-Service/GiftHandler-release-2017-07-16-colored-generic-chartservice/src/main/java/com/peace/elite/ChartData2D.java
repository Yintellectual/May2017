package com.peace.elite;

import lombok.Data;

@Data
public class ChartData2D{
	public ChartData2D(){}
	public ChartData2D(String[] labels, Long[] data, String[]color){
		this.labels = labels;
		this.data = data;
		this.color = color;
	}
	private String[] labels;
	private Long[] data;
	private String[] color;
}