package com.yibee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Collect;
import com.yibee.entity.CollectWithGoodsAndMember;
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.NameOrder;

public interface CollectRepository  extends PagingAndSortingRepository<Collect, Long>{
	 @Query(value = "from Collect c where c.goodsId=?1 and c.memberId = ?2")
	 Optional<Collect> findByGoodsidAndMemberid(Long goodsId,Long memberId);
	 
	 /*
	  @Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr,o.orderNo) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and g.name like ?2",
			countQuery=" select count(o.id)  from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and g.name like ?2")
	Page<NameOrder> findBuyerNameOrderLikeName(Long buyerId,String name,Pageable pageable);
	
	
	  @Query(value = "select new com.yibee.entity.GoodsWithMember(g,m) from Goods g left join Member m on g.sellerId=m.id where g.sellerId=?1 and g.status = ?2 and g.name like ?3")
    Page<GoodsWithMember> findGMBySelleridAndStatus(Long sellerId, int status,String name,Pageable pageable);
	  */
	 @Query(value="select new com.yibee.entity.CollectWithGoodsAndMember(c,g,m) from Collect c inner join Goods g on c.goodsId = g.id inner join Member m on g.sellerId = m.id where c.memberId = ?1")
	 Page<CollectWithGoodsAndMember> findCGMByUID(Long uid,Pageable pageable);
}
