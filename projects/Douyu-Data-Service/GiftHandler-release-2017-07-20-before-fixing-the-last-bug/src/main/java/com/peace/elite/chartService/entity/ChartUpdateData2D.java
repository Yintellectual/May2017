package com.peace.elite.chartService.entity;

import com.peace.elite.ChartDataServiceFor2DimensionalCharts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartUpdateData2D{
	private long index;
	private String label;
	private long data;
	private String color;
}