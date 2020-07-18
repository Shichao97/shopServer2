package com.yibee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.OrderWithGoods;


public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>{
	@Query(value = "select MAX(g.id) FROM Goods g")
	public Long getMaxId();
	
	@Query(value = "from Goods g where g.id = ?1")
	Goods findGoodsById(Long id);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.id = ?1")
	Optional<GoodsWithMember> findGMById(Long id);
	
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<Goods> findBySelleridAndStatusAndName(Long sellerId, int status,String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.sellerId=?1 and g.status = ?2 and g.description like ?3")
    Page<Goods> findBySelleridAndStatusAndDesc(Long sellerId, int status,String desc,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.name like ?1")
    Page<Goods> findByStatusAndName(String name,Pageable pageable);
	
	@Query(value = "from Goods g where g.status = 1 and g.description like ?1")
    Page<Goods> findByStatusAndDesc(String desc,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<GoodsWithMember> findGMBySellerAndStatusAndName(Long sellerId, int status,String name,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.status = 1 and g.name like ?1")
    Page<GoodsWithMember> findSellingGMByName(String name,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.status = 1 and g.name like ?1 and m.schoolCode=?2")
    Page<GoodsWithMember> findSellingGMByNameAndSchool(String name,String schoolCode,Pageable pageable);

	@Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.sellerId=?1 and g.status = 1 and g.name like ?2")
    Page<GoodsWithMember> findSellingGMBySellerAndName(Long sellerId,String name,Pageable pageable);

	//在售
	@Query(value = "from Goods g where g.status = 1 and g.sellerId=?1")
    Page<Goods> findSellingNow(Long sellerId, Pageable pageable);
	
	//在途
	@Query(value="select g from Goods g inner join Order o on g.id=o.goodsId where o.status = 0 and g.sellerId=?1")
	Page<Goods> findOnTheWay(Long sellerId, Pageable pageable);
	
	//已售
	@Query(value="select g from Goods g inner join Order o on g.id=o.goodsId where o.status = 1 and g.sellerId=?1")
	Page<Goods> findSold(Long sellerId, Pageable pageable);
	
	//下架
	@Query(value = "from Goods g where g.status = 0 and g.sellerId=?1")
    Page<Goods> findRemoveOff(Long sellerId, Pageable pageable);

}
