package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.AudienceRankList;
import com.peace.elite.entities.BigGift;


@RepositoryRestResource(collectionResourceRel = "bigGift", path = "bigGift")
public interface BigGiftRepository extends PagingAndSortingRepository<BigGift,Long>{
	
}