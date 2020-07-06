package com.yibee.entity;

public class OrderWithGoods {
	private Order order;
	private Goods goods;
	public OrderWithGoods() {
		
	}
	
	public OrderWithGoods(Order order,Goods goods) {
		this.order = order;
		this.goods = goods;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
}
