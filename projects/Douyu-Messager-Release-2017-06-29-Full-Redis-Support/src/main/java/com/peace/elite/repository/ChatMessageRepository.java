package com.peace.elite.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.peace.elite.entities.ChatMessage;

@RepositoryRestResource(collectionResourceRel = "chatMessage", path = "chatMessage")
public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, Long>{
	
}