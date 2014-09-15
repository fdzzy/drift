package com.drift.bean;

public class Bottle {
	
	private int bottleId = 0;
	private int senderId = 0;
	private int receiverId = 0;
	private long sendTime = 0l;
	private String content = null;
	
	public Bottle() {
	}
	
	public Bottle(int bottleId, int senderId, int receiverId, long sendTime, String content) {
		this.bottleId = bottleId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.sendTime = sendTime;
		this.content = content;
	}
	
	public int getBottleId() {
		return bottleId;
	}

	public int getSenderId() {
		return senderId;
	}
	
	public int getReceiverId() {
		return receiverId;
	}
	
	public long getSendTime() {
		return sendTime;
	}
	
	public String getContent() {
		return content;
	}

}


