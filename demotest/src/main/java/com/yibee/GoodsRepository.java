package com.yibee;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{

}
