package com.peace.elite.entities;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@Data
@NoArgsConstructor
public class ChartEntry2D implements Comparable<ChartEntry2D> {

	private static Random random = new Random();
	protected AtomicLong data = new AtomicLong(0l);
	
	protected String label;
	protected String id;//helper
	protected String color = "rgba("+(long)random.nextInt(255)+","+(long)random.nextInt(255)+","+(long)random.nextInt(255)+",0.7)";
	public ChartEntry2D(Long data, String label, String id){
		setData(data);;
		this.label=label;
		this.id=id;
	}

	public long getData(){
		return data.get();
	}
	public void setData(long newValue){
		this.data.set(newValue);;
	}
	
	public void increase(Long amount){
		data.addAndGet(amount);
	}
	
	
	@Override
	public int compareTo(ChartEntry2D o) {
		// TODO Auto-generated method stub
		return (int)(this.data.get() - (o.getData()));
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
	
	public ChartEntry2D clone(){
		// TODO Auto-generated method stub
		ChartEntry2D result = new ChartEntry2D(data.get(), label, id);
		result.setColor(color);
		return result;
	}
}
