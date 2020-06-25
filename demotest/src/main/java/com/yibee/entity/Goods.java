package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the goods database table.
 * 
 */
@Entity
@Table(name="goods")
@NamedQuery(name="Goods.findAll", query="SELECT g FROM Goods g")
public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String location;

	private String name;

	private float price;

	@Column(name="seller_id")
	private Long sellerId;

	@Column(name="selling_method")
	private byte sellingMethod;

	private int status;
	
	private String description;
	
	@Column(name="img_names")
	private String imgNames;

	@Column(name="type_code")
	private String typeCode;

	public Goods() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public byte getSellingMethod() {
		return this.sellingMethod;
	}

	public void setSellingMethod(byte sellingMethod) {
		this.sellingMethod = sellingMethod;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImgNames() {
		return this.imgNames;
	}

	public void setImgNames(String imgNames) {
		this.imgNames = imgNames;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}