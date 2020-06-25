package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
	
	@Query(value = "from Goods g where g.id = ?1")
	Goods findGoodsById(Long id);
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<Goods> findBySelleridAndStatusAndName(Long sellerId, int status,String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.description like ?3")
    Page<Goods> findBySelleridAndStatusAndDesc(Long sellerId, int status,String desc,Pageable pageable);
	
	
}
