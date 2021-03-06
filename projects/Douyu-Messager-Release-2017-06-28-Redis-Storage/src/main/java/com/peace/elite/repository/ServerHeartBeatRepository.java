package com.peace.elite.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.ServerHeartBeat;

@RepositoryRestResource(collectionResourceRel = "serverHeartBeat", path = "serverHeartBeat")
public interface ServerHeartBeatRepository extends PagingAndSortingRepository<ServerHeartBeat, Long>{
	
}