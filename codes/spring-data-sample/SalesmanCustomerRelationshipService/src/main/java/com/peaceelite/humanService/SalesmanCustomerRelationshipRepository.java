package com.peaceelite.humanService;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.RepositoryDefinition;


@RepositoryRestResource(collectionResourceRel = "salesmanCustomerRelationship", path = "salesmanCustomerRelationship")
public interface SalesmanCustomerRelationshipRepository extends CrudRepository<SalesmanCustomerRelationship, Long> {

	List<SalesmanCustomerRelationship> findByLastName(@Param("name") String name);

	List<SalesmanCustomerRelationship> findAll();
	
	List<SalesmanCustomerRelationship> findByNickName(@Param("nickName") String nickName);
	
	Long countByLastName(@Param("lastName") String lastName);
	
	@Modifying//which means we are modifying an existing row, and only part of the information is necessary
	@Transactional
	@Query("update SalesmanCustomerRelationship salesmanCustomerRelationship set salesmanCustomerRelationship.firstName = :firstName where salesmanCustomerRelationship.lastName = :lastName")
	int setFirstNameForcSalesmanCustomerRelationship(@Param("firstName") String firstName,@Param("lastName") String lastName);
}