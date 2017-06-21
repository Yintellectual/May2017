package com.peace.elite.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.OnlineGift;

@RepositoryRestResource(collectionResourceRel = "onlineGift", path = "onlineGift")
public interface OnlineGiftRepository extends PagingAndSortingRepository<OnlineGift, Long>{
	
}