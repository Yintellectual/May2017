package com.peaceelite.customerService;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryRestResource
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	Customer save(Customer customer);
	long count();
	
	@Modifying//which means we are modifying an existing row, and only part of the information is necessary
	@Transactional
	@Query("update Customer customer set customer.name = :newName where customer.name = :oldName")
	int updateName(@Param("oldName") String oldName, @Param("newName") String newName);	
}