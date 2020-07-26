package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;


/**
 * The persistent class for the orders database table.
 * 
 */
@Entity
@Table(name="orders")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	public static final int PAYMENT_NO = 0;
	public static final int PAYMENT_YES = 1;
	
	public static final int STATUS_WAIT_COMPLETE = 0;
	public static final int STATUS_COMPLETED = 1;
	public static final int STATUS_CANCELED = -1;
	
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="buyer_id")
	private Long buyerId;

	@Column(name="buyer_name")
	private String buyerName;

	@Column(name="goods_id")
	private Long goodsId;

	@Column(name="order_price")
	private float orderPrice;


	@JsonSerialize(using = DateToLongSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="order_time")
	private Date orderTime;

	@Column(name="payment_status")
	private int paymentStatus;

	@Column(name="receive_addr")
	private String receiveAddr;
	
	@Column(name="receive_method")
	private byte receiveMethod;

	@Column(name="seller_id")
	private Long sellerId;

	@Column(name="seller_name")
	private String sellerName;

	@Column(name="order_no")
	private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	private int status;

	public Order() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyerId() {
		return this.buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return this.buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public float getOrderPrice() {
		return this.orderPrice;
	}

	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Date getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public int getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getReceiveAddr() {
		return this.receiveAddr;
	}

	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}
	
	public byte getReceiveMethod() {
		return this.receiveMethod;
	}

	public void setReceiveMethod(byte receiveMethod) {
		this.receiveMethod = receiveMethod;
	}

	public Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return this.sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}