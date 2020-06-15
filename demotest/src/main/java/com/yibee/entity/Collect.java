package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the collect database table.
 * 
 */
@Entity
@NamedQuery(name="Collect.findAll", query="SELECT c FROM Collect c")
public class Collect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="goods_id")
	private Long goodsId;

	@Column(name="member_id")
	private Long memberId;

	public Collect() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

}