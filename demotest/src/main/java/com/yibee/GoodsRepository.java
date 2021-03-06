package com.yibee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Goods;
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.GoodsWithOrder;
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

	
	//selling now
	@Query(value = "select new com.yibee.entity.GoodsWithOrder(g,o) from Goods g left join Order o on g.id=o.sellerId where g.status = 1 and g.sellerId=?1")
    Page<GoodsWithOrder> findSellingNow(Long sellerId, Pageable pageable);
	
	//on the way
	@Query(value="select new com.yibee.entity.GoodsWithOrder(g,o) from Goods g inner join Order o on g.id=o.goodsId where o.status = 0 and g.sellerId=?1")
	Page<GoodsWithOrder> findOnTheWay(Long sellerId, Pageable pageable);
	
	//sold out
	@Query(value="select new com.yibee.entity.GoodsWithOrder(g,o) from Goods g inner join Order o on g.id=o.goodsId where o.status = 1 and g.sellerId=?1")
	Page<GoodsWithOrder> findSold(Long sellerId, Pageable pageable);
	
	//remove off from shelf
	@Query(value = "select new com.yibee.entity.GoodsWithOrder(g,o) from Goods g left join Order o on g.id=o.sellerId where g.status = 0 and g.sellerId=?1")
    Page<GoodsWithOrder> findRemoveOff(Long sellerId, Pageable pageable);


	//selling count
	@Query(value = "select count(g) from Goods g where g.status = 1 and g.sellerId=?1")
    int getSellingCount(Long sellerId);
	
	//not finish count
	@Query(value="select count(g) from Goods g inner join Order o on g.id=o.goodsId where o.status = 0 and g.sellerId=?1")
	int getOnTheWayCount(Long sellerId);

}
