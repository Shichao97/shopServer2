package com.yibee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
}
