package com.drift.servlet;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.*;
import javax.websocket.server.*;

import com.drift.util.HTMLFilter;

@ServerEndpoint(value="/chat")
public class WebSocketChat extends Endpoint
{
	private static final String GUEST_PREFIX = "Guest";
	private static final AtomicInteger connectionsIds = new AtomicInteger(0);
	private static final Set<WebSocketChat> connections = 
			new CopyOnWriteArraySet<WebSocketChat>();
	
	private String nickname;
	private Session session;
	
	public WebSocketChat() {
		nickname = GUEST_PREFIX + connectionsIds.getAndIncrement();
	}
	
	@Override
	public void onOpen (Session session, EndpointConfig config) {
		System.out.println("Connection opened");
		final RemoteEndpoint.Basic remote = session.getBasicRemote();
		this.session = session;
		
		try {
			remote.sendText("HeartBeatRequest");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			public void onMessage (String text) {
				try {
					//System.out.println(text);
					int pos = text.indexOf("HeartBeatReply:");
					if(pos != -1) {
						String username = text.substring(pos + "HeartBeatReply:".length());
						//System.out.println(username);
						nickname = username;
						remote.sendText(nickname + "，您已加入聊天室");
					} else {
						//remote.sendText("Got your message (" + text + "). Thanks!");
						String filteredMessage = String.format("%s: %s", 
								nickname, HTMLFilter.filter(text));
						broadcast(filteredMessage);
						//remote.sendText(filteredMessage);
					}
				} catch (Exception ioex) {
					System.out.println("onMessage error: "+ioex.getMessage());
				}
			}
		});
		
		connections.add(this);
		//String message = String.format("* %s %s", nickname, "has joined!");
		//broadcast(message);
		
	}

	@Override
	public void onClose (Session session, CloseReason reason) {
		System.out.println("Connection closed: "+reason);
		connections.remove(this);
		String message = String.format("* %s %s", nickname, "has disconnected.");
		broadcast(message);
	}

	@Override
	public void onError (Session session, Throwable thr) {
		System.out.println("Error: "+thr.getMessage());
		thr.printStackTrace();
	}
	
	private static void broadcast(String msg) {
		for(WebSocketChat client : connections) {
			try {
				synchronized (client) {
					client.session.getBasicRemote().sendText(msg);					
				}				
			} catch (IOException e) {
				// TODO: handle exception
				System.err.println("Chat error: Failed to send message to client");
				connections.remove(client);
				try {
					client.session.close();
				} catch (IOException e2) {
					// TODO: handle exception
				}
				String message = String.format("* %s %s", client.nickname, "has been disconnected.");
				broadcast(message);
			}
		}
	}
}
