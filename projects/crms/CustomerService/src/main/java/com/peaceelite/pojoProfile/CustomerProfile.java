package com.peaceelite.pojoProfile;

import com.peaceelite.customerService.Customer;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;
import lombok.NonNull;

@Data
public class CustomerProfile{
	
	
	@NonNull 
	private Long id;
	@NonNull 
	private String name;
	
	public Customer getCustomer(){
		//placeholder
		return new Customer();
	}	
}