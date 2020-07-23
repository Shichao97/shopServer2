package com.yibee.entity;

public class GoodsWithOrder {
	Goods g;
	Order o;
	public GoodsWithOrder(Goods g, Order o) {
		super();
		this.g = g;
		this.o = o;
	}
	public Goods getG() {
		return g;
	}
	public void setG(Goods g) {
		this.g = g;
	}
	public Order getO() {
		return o;
	}
	public void setO(Order o) {
		this.o = o;
	}
	
}
