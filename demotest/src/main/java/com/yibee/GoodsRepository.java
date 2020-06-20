package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
	
	@Query(value = "from Goods g where g.status = 0 and g.name like ?1")
    Page<Goods> findByStatus0Name(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 0 and g.description like ?1")
    Page<Goods> findByStatus0Desc(String desc,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.name like ?1")
    Page<Goods> findByStatus1Name(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.description like ?1")
    Page<Goods> findByStatus1Desc(String desc,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 2 and g.name like ?1")
    Page<Goods> findByStatus2Name(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 2 and g.description like ?1")
    Page<Goods> findByStatus2Desc(String desc,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = -1 and g.name like ?1")
    Page<Goods> findByStatusm1Name(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = -1 and g.description like ?1")
    Page<Goods> findByStatusm1Desc(String desc,Pageable pageable);
	
}
