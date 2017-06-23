package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.LuckyMoney;


@RepositoryRestResource(collectionResourceRel = "luckyMoney", path = "luckyMoney")
public interface LuckyMoneyRepository extends PagingAndSortingRepository<LuckyMoney,Long>{
	
}