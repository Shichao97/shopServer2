package com.yibee.entity;

public class CollectWithGoodsAndMember {
	private Collect collect;
	private Goods goods;
	private Member member;
	
	public CollectWithGoodsAndMember() {
		
	}
	
	public CollectWithGoodsAndMember(Collect collect,Goods goods,Member member) {
		this.collect = collect;
		this.goods = goods;
		this.member = member;
	}

	public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
}
