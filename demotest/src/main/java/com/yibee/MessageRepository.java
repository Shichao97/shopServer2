package com.yibee;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Message;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{

}
