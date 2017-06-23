package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.AudienceRankList;


@RepositoryRestResource(collectionResourceRel = "audienceRankList", path = "audienceRankList")
public interface AudienceRankListRepository extends PagingAndSortingRepository<AudienceRankList, Long>{
	
}