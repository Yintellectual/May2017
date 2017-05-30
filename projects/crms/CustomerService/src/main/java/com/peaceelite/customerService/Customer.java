package com.peaceelite.customerService;

import com.peaceelite.pojoProfile.CustomerProfile;
import com.peaceelite.pojoProfile.RequirementProfile;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import java.util.*;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Customer{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NonNull
	private String name;
	
	
	private List<RequirementProfile> requirementProfiles = new LinkedList<RequirementProfile>();
	public addRequirementProfile(RequirementProfile rp){
		requirementProfiles.add(rp);
	}
	public removeRequirementProfile(RequirementProfile rp){
		requirementProfiles.remove(rp);
	}
		
	
	@Transient
	//@Getter(AccessLevel.NONE)
	//@Setter(AccessLevel.NONE)
	private CustomerProfile profile;
	
	
	private CustomerProfile getCustomerProfile(){
		if(this.id==null||this.name==null){
			throw new IllegalArgumentException("The customer id or name can not be NULL.");
		}
		return new CustomerProfile(this.id,this.name);
	}
	
	
	@DomainEvents
	Collection<Object> domainEvents() {
        List<Object> result = new ArrayList<Object>();
		result.add("Here should be an Event not a String, but, anyway");
		return result; 
	}
	
	@AfterDomainEventPublication 
    void callbackMethod() {
		System.out.println("Id="+this.getId()+"11111111111111111111111111111"+"WELL DONE");
	}

}