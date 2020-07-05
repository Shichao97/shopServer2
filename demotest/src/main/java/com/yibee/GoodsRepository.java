package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;
import com.yibee.entity.GoodsWithMember;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
	
	@Query(value = "from Goods g where g.id = ?1")
	Goods findGoodsById(Long id);
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<Goods> findBySelleridAndStatusAndName(Long sellerId, int status,String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.description like ?3")
    Page<Goods> findBySelleridAndStatusAndDesc(Long sellerId, int status,String desc,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.name like ?1")
    Page<Goods> findByStatusAndName(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.description like ?1")
    Page<Goods> findByStatusAndDesc(String desc,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<GoodsWithMember> findGMBySelleridAndStatus(Long sellerId, int status,String name,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.status = 1 and g.name like ?1")
    Page<GoodsWithMember> findGMByStatus(String name,Pageable pageable);

}
