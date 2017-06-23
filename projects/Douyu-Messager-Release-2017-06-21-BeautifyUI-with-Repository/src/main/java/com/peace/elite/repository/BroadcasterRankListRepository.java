package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.BroadcasterRankList;


@RepositoryRestResource(collectionResourceRel = "broadcasterRankList", path = "broadcasterRankList")
public interface BroadcasterRankListRepository extends PagingAndSortingRepository<BroadcasterRankList,Long>{
	
}