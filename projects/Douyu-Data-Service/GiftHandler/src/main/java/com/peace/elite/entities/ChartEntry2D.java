package com.peace.elite.entities;

import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ChartEntry2D implements Comparable<ChartEntry2D> {

	private static Random random = new Random();
	protected Long data = 0l;
	protected String label;
	protected String id;//helper
	protected String color = "rgba("+(long)random.nextInt(255)+","+(long)random.nextInt(255)+","+(long)random.nextInt(255)+",1)";
	public ChartEntry2D(Long data, String label, String id){
		this.data=data;
		this.label=label;
		this.id=id;
	}
	
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
