package com.drift.core;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class NotificationMessage implements Serializable, JSONAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private int uid = 0;
	private int senderId = 0;
	private String senderName = null;
	private String content = null;
	private Timestamp ts = null;
	private int unreadCount = 0;
	
	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	
	public NotificationMessage() {
	}
	
	public NotificationMessage(int senderID, String senderName, String content, Timestamp ts, int unreadCount) {
		this.senderId = senderID;
		this.senderName = senderName;
		this.content = content;
		this.ts = ts;
		this.unreadCount = unreadCount;
	}

	@Override
	public String toJSONString() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//JSONObject obj = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("senderId", senderId);
		map.put("senderName", senderName);
		map.put("content", content);
		map.put("ts", sdf.format(ts));
		map.put("unreadCount", unreadCount);
		
		JSONObject obj = new JSONObject(map);
		
		//obj.put("senderId", senderId);
		//obj.put("receiverId", receiverId);
		//obj.put("content", content);
		//obj.put("ts", sdf.format(ts));
		return obj.toJSONString();
	}
	
	

}
