package com.peace.elite.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.peace.elite.partition.Partition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Giving implements Comparable<Giving> {

	private long uid;
	private long gid;
	private long timeStamp;
	private String userName;

	@Override
	public int compareTo(Giving o) {
		// TODO Auto-generated method stub
		// int result = (int) ((timeStamp - o.getTimeStamp()));
		// if(result == 0){
		long result = uid - o.getUid();
		if (result > 0) {
			return 1;
		} else if (result < 0) {
			return -1;
		} else {
			return 0;
		}
		// }else{
		// return result;
		// }
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		try {
			if (o == null || o instanceof Giving) {

				result = false;
			}else{
				result = (uid == ((Giving) o).getUid());
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(100));
			} catch (Exception ex) {
			}
			equals(o);			
		}
		return result;
	}

	@Override
	public int hashCode() {
		return new Long(uid).hashCode();
	}
}
