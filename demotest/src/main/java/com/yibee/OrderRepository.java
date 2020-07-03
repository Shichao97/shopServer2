package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Order;
import com.yibee.entity.NameOrder;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long>{
//	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id) from Goods g  inner join Order o where g.id = o.buyerId",
//			countQuery=" select count(g.name)  from Goods g  inner join Order o where g.id = o.buyerId")
//	Page<NameOrder> findNameOrder(Pageable pageable);

	
	
	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr) from Goods g  inner join Order o where g.id = o.buyerId",
			countQuery=" select count(g.name)  from Goods g  inner join Order o where g.id = o.buyerId")
	Page<NameOrder> findNameOrder(Pageable pageable);

	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr) from Goods g  inner join Order o where g.id = o.buyerId and g.name like ?1",
			countQuery=" select count(g.name)  from Goods g  inner join Order o where g.id = o.buyerId and g.name like ?1")
	Page<NameOrder> findNameOrderLikeName(String name,Pageable pageable);

}
