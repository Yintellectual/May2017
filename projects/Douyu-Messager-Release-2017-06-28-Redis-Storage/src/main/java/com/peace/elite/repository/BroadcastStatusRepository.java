package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.BroadcastStatus;


@RepositoryRestResource(collectionResourceRel = "broadcastStatus", path = "broadcastStatus")
public interface BroadcastStatusRepository extends PagingAndSortingRepository<BroadcastStatus,Long>{
	
}