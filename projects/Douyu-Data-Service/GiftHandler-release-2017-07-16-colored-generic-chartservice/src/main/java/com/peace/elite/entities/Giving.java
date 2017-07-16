package com.peace.elite.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Giving implements Comparable<Giving>{
	
	private long uid;
	private long gid;
	private long timeStamp;
	private String userName;

	@Override
	public int compareTo(Giving o) {
		// TODO Auto-generated method stub
		return (int)((timeStamp - o.getTimeStamp())/(long)Integer.MAX_VALUE);
	}

	@Override
	public boolean equals(Object o){
		return this == o;
	}
	
	@Override
	public int hashCode(){
		return new Long(timeStamp).hashCode();
	}

}
