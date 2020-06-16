package com.yibee;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.GoodsType;

public interface GoodstypeRepository extends PagingAndSortingRepository<GoodsType, Integer>{
	/*
	@Query(value = "from GoodsType t order by t.categoryCode")
	List<GoodsType> findAllOrderByCategory();
	*/
}
