package com.yibee.entity;

public class CollectWithGoodsAndMember {
	private Collect collect;
	private Goods g;
	private Member m;
	
	public CollectWithGoodsAndMember() {
		
	}
	
	public CollectWithGoodsAndMember(Collect collect,Goods goods,Member member) {
		this.collect = collect;
		this.g = goods;
		this.m = member;
	}

	public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
	}

	public Goods getG() {
		return g;
	}

	public void setG(Goods goods) {
		this.g = goods;
	}

	public Member getM() {
		return m;
	}

	public void setM(Member member) {
		this.m = member;
	}
	
	
}
