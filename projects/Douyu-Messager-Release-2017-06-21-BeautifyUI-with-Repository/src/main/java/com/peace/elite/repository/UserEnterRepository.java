package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.UserEnter;


@RepositoryRestResource(collectionResourceRel = "userEnter", path = "userEnter")
public interface UserEnterRepository extends PagingAndSortingRepository<UserEnter,Long>{
	
}