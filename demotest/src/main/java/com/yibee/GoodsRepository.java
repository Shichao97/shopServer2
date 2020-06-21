package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
	
	@Query(value = "from Goods g where g.status = ?1 and g.name like ?2")
    Page<Goods> findByStatusAndName(int status,String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = ?1 and g.description like ?2")
    Page<Goods> findByStatusAndDesc(int status,String desc,Pageable pageable);
	
	
}
