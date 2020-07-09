package com.yibee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the messages database table.
 * 
 */
@Entity
@Table(name="messages")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String content;

	@Column(name="not_read")
	private int notRead;

	@Column(name="to_id")
	private Long toId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_time")
	private Date sendTime;

	@Column(name="from_id")
	private Long fromId;

	@Column(name="from_name")
	private String fromName;

	public Message() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNotRead() {
		return this.notRead;
	}

	public void setNotRead(int hasRead) {
		this.notRead = hasRead;
	}

	public Long getToId() {
		return this.toId;
	}

	public void setToId(Long receiverId) {
		this.toId = receiverId;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Long getFromId() {
		return this.fromId;
	}

	public void setFromId(Long senderId) {
		this.fromId = senderId;
	}

	public String getFromName() {
		return this.fromName;
	}

	public void setFromName(String senderName) {
		this.fromName = senderName;
	}

}