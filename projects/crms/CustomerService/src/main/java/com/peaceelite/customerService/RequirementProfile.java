package com.peaceelite.customerService;


import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class RequirementProfile{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@NonNull 
	private Long requirementId;
	@NonNull 
	private String name;
	
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof RequirementProfile)
			return ((RequirementProfile)obj).getRequirementId().equals(this.requirementId);
		else
			return false;
	}
}