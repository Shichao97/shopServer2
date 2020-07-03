package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Order;
import com.yibee.entity.NameOrder;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long>{

	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr) from Goods g  inner join Order o where o.buyerId=?1 and g.id = o.goodsId ",
			countQuery=" select count(g)  from Goods g  inner join Order o where  o.buyerId=?1 and g.id = o.goodsId")
	Page<NameOrder> findBuyerNameOrder(Long buyerId,Pageable pageable);

	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr) from Goods g  inner join Order o where o.buyerId=?1 and g.id = o.goodsId and g.name like ?2",
			countQuery=" select count(g)  from Goods g  inner join Order o where o.buyerId=?1 and g.id = o.goodsId and g.name like ?2")
	Page<NameOrder> findBuyerNameOrderLikeName(Long buyerId,String name,Pageable pageable);

}
