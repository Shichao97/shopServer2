package com.yibee.entity;

import java.util.Date;



public class NameOrder {
	private static final long serialVersionUID = 1L;
	private String goodsName;
	private Long id;

	private String imgNames;
	private Long buyerId;
	private String buyerName;

	private Long goodsId;

	private float orderPrice;

	private Date orderTime;

	private int paymentStatus;

	private String receiveAddr;

	private Long sellerId;

	private String sellerName;

	private int status;

	private String orderNo;

	public NameOrder(		
			String goodsName
			) 
	{
		this.goodsName = goodsName;
	}
	
	
	public NameOrder(		
			Long goodsId,
			String goodsName,
			Long id
			) 
	{
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.id = id;
		
	}

	public NameOrder(
		Long goodsId,
		String goodsName,
		String imgNames,
		Long id,
		Long buyerId,
		String buyerName,
		Long sellerId,
		String sellerName,
		int paymentStatus,
		int status,
		java.util.Date orderTime,
		float orderPrice,
		String receiveAddr,
		String orderNo
		) 
	{
			this.goodsId = goodsId;
			this.goodsName = goodsName;
			this.imgNames = imgNames;
			this.id = id;
			this.buyerId = buyerId;
			this.buyerName = buyerName;
			this.sellerId = sellerId;
			this.sellerName = sellerName;
			this.paymentStatus = paymentStatus;
			this.status = status;
			this.orderTime = orderTime;
			this.orderPrice = orderPrice;
			this.receiveAddr = receiveAddr;
			this.orderNo = orderNo;
	}		
		
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getReceiveAddr() {
		return receiveAddr;
	}

	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public NameOrder() {
		
	}


	public String getImgNames() {
		return imgNames;
	}


	public void setImgNames(String imgNames) {
		this.imgNames = imgNames;
	}
	

	
}
