package com.drift.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import com.drift.service.UserService;
import com.drift.service.impl.ServiceFactory;

public class ChatMessage implements Serializable, JSONAware {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int messageId;
	private int senderId;
	private int receiverId;
	//private User sender;
	//private User receiver;
	private long ts;
	private String content;	
	
	public ChatMessage() {		
	}
	
	public ChatMessage(int messageId, int senderId, int receiverId, long ts, String content) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.ts = ts;
		this.content = content;
	}

	public int getMessageId() {
		return messageId;
	}

	public long getTs() {
		return ts;
	}

	public String getContent() {
		return content;
	}

	public int getSenderId() {
		return senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	@Override
	public String toJSONString() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//JSONObject obj = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		
		//TODO: this is really ugly to put this service code into the bean class
		UserService service = ServiceFactory.createUserService();
		User sender = (User) service.getUserById(senderId).getResultObject();
		User receiver = (User) service.getUserById(receiverId).getResultObject();
		
		map.put("senderId", senderId);
		map.put("senderName", sender.getUsername());
		map.put("receiverId", receiverId);
		map.put("receiverName", receiver.getUsername());
		map.put("content", content);
		map.put("ts", sdf.format(ts));
		
		JSONObject obj = new JSONObject(map);
		
		//obj.put("senderId", senderId);
		//obj.put("receiverId", receiverId);
		//obj.put("content", content);
		//obj.put("ts", sdf.format(ts));
		return obj.toJSONString();
	}


	


}
