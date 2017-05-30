package com.peaceelite.pojoProfile;


import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;
import lombok.NonNull;

@Data
public class RequirementProfile{
	
	
	@NonNull 
	private Long id;
	@NonNull 
	private String name;
	
	
	//@Override
	//public boolean equals()
	 
}