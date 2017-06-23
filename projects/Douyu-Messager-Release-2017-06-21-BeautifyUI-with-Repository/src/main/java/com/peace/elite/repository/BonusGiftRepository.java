package com.peace.elite.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.BonusGift;


@RepositoryRestResource(collectionResourceRel = "bonusGift", path = "bonusGift")
public interface BonusGiftRepository extends PagingAndSortingRepository<BonusGift,Long>{
	
}