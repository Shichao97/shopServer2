package com.yibee;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long>{

}
