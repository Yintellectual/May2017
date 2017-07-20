package com.peace.elite.eventListener;

import com.peace.elite.entities.ChartEntry2D;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<T extends Comparable<T>>  implements Comparable<Event<T>>{
	private T data;

	@Override
	public int compareTo(Event<T> o) {
		// TODO Auto-generated method stub
		return (int)(this.data.compareTo(o.getData()));
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		try {
			if (o != null && o instanceof Event<?>) {

				return this.data.equals(((Event<T>) o).getData());
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}