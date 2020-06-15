package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the goods_images database table.
 * 
 */
@Entity
@Table(name="goods_images")
@NamedQuery(name="GoodsImage.findAll", query="SELECT g FROM GoodsImage g")
public class GoodsImage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="goods_id")
	private Long goodsId;

	@Column(name="image_path")
	private String imagePath;

	public GoodsImage() {
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

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}