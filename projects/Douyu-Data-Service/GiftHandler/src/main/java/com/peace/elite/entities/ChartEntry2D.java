package com.peace.elite.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartEntry2D implements Comparable<ChartEntry2D> {

	private Long data;
	private String label;
	private String id;//helper

	@Override
	public int compareTo(ChartEntry2D o) {
		// TODO Auto-generated method stub
		return this.data.compareTo(o.data);
	}

	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		try {
			if (o != null && o instanceof ChartEntry2D) {

				return this.id.equals(((ChartEntry2D) o).id);
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public ChartEntry2D addOn(ChartEntry2D oldVersion){
		this.data = this.data+oldVersion.getData();
		return this;
	}
}
