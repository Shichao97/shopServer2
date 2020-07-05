package com.yibee.entity;

public class GoodsWithMember {

	private Goods g;


	private Member m;
	public GoodsWithMember() {
		
	}
	public GoodsWithMember(Goods g,Member m) {
		this.g = g;
		this.m = m;
	}
	
	public Goods getG() {
		return g;
	}
	public void setG(Goods g) {
		this.g = g;
	}
	public Member getM() {
		return m;
	}
	public void setM(Member m) {
		this.m = m;
	}
}
