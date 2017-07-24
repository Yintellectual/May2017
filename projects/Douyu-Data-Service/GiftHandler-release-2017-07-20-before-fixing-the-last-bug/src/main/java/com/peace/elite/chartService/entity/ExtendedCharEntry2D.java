package com.peace.elite.chartService.entity;

import com.peace.elite.entities.ChartEntry2D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedCharEntry2D extends ChartEntry2D{
	private long oldData;
	public long getOldData(){
		return oldData;
	}
	public void setOldData(long oldData){
		this.oldData = oldData;
	}
	public ExtendedCharEntry2D(long oldData, ChartEntry2D entry){
		this.color = entry.getColor();
		this.oldData = oldData;
		this.setData(entry.getData());
		this.label = entry.getLabel();
		this.id = entry.getId();
	}
}