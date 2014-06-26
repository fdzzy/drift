package com.drift.core;

public class Bottle {
	
	private int bottleId = 0;
	private int senderId = 0;
	private String senderName = null;
	private String content = null;
	
	public Bottle() {
	}
	
	public Bottle(int bottleId, int senderId, String senderName, String content)
	{
		this.bottleId = bottleId;
		this.senderId = senderId;
		this.senderName = senderName;
		this.content = content;
	}
	
	public int getBottleId() {
		return bottleId;
	}

	public void setBottleId(int bottleId) {
		this.bottleId = bottleId;
	}
	
	public int getSenderId() {
		return senderId;
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public String getContent() {
		return content;
	}

}


