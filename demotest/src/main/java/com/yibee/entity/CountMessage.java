package com.yibee.entity;

public class CountMessage {
	private long count;
	private Long toId;
	private Long fromId;
	private Long otherId;
	private String otherName;
	
	public CountMessage(Long toId, Long fromId, Long otherId,String otherName) {
		super();
		this.count = 0;
		this.toId = toId;
		this.fromId = fromId;
		this.otherId = otherId;
		this.otherName = otherName;
	}

	public CountMessage(long count, Long toId, Long fromId, Long otherId,String otherName) {
		super();
		this.count = count;
		this.toId = toId;
		this.fromId = fromId;
		this.otherId = otherId;
		this.otherName = otherName;
	}
	
	
	public Long getOtherId() {
		return otherId;
	}

	public void setOtherId(Long myId) {
		this.otherId = myId;
	}

	public CountMessage() {
		super();
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public Long getToId() {
		return toId;
	}
	public void setToId(Long toId) {
		this.toId = toId;
	}
	public Long getFromId() {
		return fromId;
	}
	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String fromName) {
		this.otherName = fromName;
	}
	
	
}
