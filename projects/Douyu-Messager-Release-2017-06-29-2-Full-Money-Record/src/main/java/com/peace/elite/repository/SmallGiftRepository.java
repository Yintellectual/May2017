package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.SmallGift;


@RepositoryRestResource(collectionResourceRel = "smallGift", path = "smallGift")
public interface SmallGiftRepository extends PagingAndSortingRepository<SmallGift,Long>{
	
}