package com.peace.elite.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Giving {

	private long uid;
	private long gid;
	private long timeStamp;
	private String userName;

}
