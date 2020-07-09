package com.yibee.entity;

public class CountMessage {
	private long count;
	private Long toId;
	private Long fromId;
	private String fromName;
	
	public CountMessage(Long toId, Long fromId, String fromName) {
		super();
		this.count = 0;
		this.toId = toId;
		this.fromId = fromId;
		this.fromName = fromName;
	}

	public CountMessage(long count, Long toId, Long fromId, String fromName) {
		super();
		this.count = count;
		this.toId = toId;
		this.fromId = fromId;
		this.fromName = fromName;
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
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	
}
