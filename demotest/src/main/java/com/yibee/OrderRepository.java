package com.yibee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Order;
import com.yibee.entity.OrderWithGoods;
import com.yibee.entity.GoodsWithMember;
import com.yibee.entity.NameOrder;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long>{

	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr,o.orderNo) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1  ",
			countQuery=" select count(o.id) from Goods g inner join Order o on g.id = o.goodsId where  o.buyerId=?1")
	Page<NameOrder> findBuyerNameOrder(Long buyerId,Pageable pageable);
	
	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr,o.orderNo) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and o.status = 0 and o.paymentStatus = 0",
			countQuery=" select count(o.id) from Goods g inner join Order o on g.id = o.goodsId where  o.buyerId=?1 and o.status = 0 and o.paymentStatus = 0")
	Page<NameOrder> findBuyerNameOrderNotPaid(Long buyerId,Pageable pageable);
	
	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr,o.orderNo) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and o.status = 0 and o.paymentStatus = 1",
			countQuery=" select count(o.id) from Goods g inner join Order o on g.id = o.goodsId where  o.buyerId=?1 and o.status = 0 and o.paymentStatus = 1")
	Page<NameOrder> findBuyerNameOrderNotFinished(Long buyerId,Pageable pageable);

	@Query(value = "select new com.yibee.entity.NameOrder(g.id,g.name,o.id,o.buyerId,o.buyerName,o.sellerId,o.sellerName,o.paymentStatus,o.status,o.orderTime,o.orderPrice,o.receiveAddr,o.orderNo) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and g.name like ?2",
			countQuery=" select count(o.id)  from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and g.name like ?2")
	Page<NameOrder> findBuyerNameOrderLikeName(Long buyerId,String name,Pageable pageable);
	
	
	@Query(value = "select MAX(o.id) FROM Order o")
	public Long getMaxId();
	
	@Query(value="select new com.yibee.entity.OrderWithGoods(o,g) from Order o inner join Goods g on o.goodsId=g.id where o.id = ?1")
	Optional<OrderWithGoods> findOGById(Long id);

	//@Query(value="select new com.yibee.entity.OrderWithGoods(o,g) from Order o inner join Goods g on o.goodsId=g.id where o.id = ?1")
	Optional<Order> findByBuyerId(Long id);
	
	
	@Query(value = "select count(o) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and o.status = 0 and o.paymentStatus = 0",
			countQuery=" select count(o.id) from Goods g inner join Order o on g.id = o.goodsId where  o.buyerId=?1 and o.status = 0 and o.paymentStatus = 0")
	long getCountNotPaid(Long buyerId);
	
	@Query(value = "select count(o) from Goods g  inner join Order o on g.id = o.goodsId where o.buyerId=?1 and o.status = 0 and o.paymentStatus = 1",
			countQuery=" select count(o.id) from Goods g inner join Order o on g.id = o.goodsId where  o.buyerId=?1 and o.status = 0 and o.paymentStatus = 1")
	long getCountNotFinished(Long buyerId);

}
