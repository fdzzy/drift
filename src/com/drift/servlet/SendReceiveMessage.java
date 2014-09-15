package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.drift.bean.ChatMessage;
import com.drift.bean.User;
import com.drift.service.MessageService;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class SendReceiveMessage
 */
@WebServlet(urlPatterns = {"/send_receive"}, asyncSupported=true)
public class SendReceiveMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService uService = ServiceFactory.createUserService();
	private MessageService mService = ServiceFactory.createMessageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendReceiveMessage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		
		User user = MyServletUtil.checkLogin(request, response);
		String action = (String)request.getParameter("action");
		PrintWriter out = response.getWriter();
		if(user == null) {
			if(action != null && action.equals("receive")) {
				out.print("false");
				out.flush();
			}
			return;
		}
		
		String friendIdStr = (String) request.getParameter("friend");
		if(friendIdStr == null) {
			System.err.println("friend is not set");
			return;
		}
		int friendId = Integer.parseInt(friendIdStr);
		if(friendId <= 0) {
			getServletContext().getRequestDispatcher(MyServletUtil.errorJspPage).forward(request, response);
			return;
		}
		User friend = null;
		if(action == null) {
			Result resultObj =  uService.getUserById(friendId);
			if(resultObj.getCode() != Result.SUCCESS) return;			
			friend = (User) resultObj.getResultObject();
			
			List<ChatMessage> messages = new ArrayList<ChatMessage>();
			resultObj =  mService.readAndFlagConversation(user.getUid(), friendId);
			if(resultObj.getCode() == Result.SUCCESS) {
				messages = (List<ChatMessage>) resultObj.getResultObject();
			}
			
			request.setAttribute("friend", friend);
			request.setAttribute("messages", messages);
			getServletContext().getRequestDispatcher(MyServletUtil.chatJspPage).forward(request, response);
		} else if(action.equals("receive")) {
			List<ChatMessage> messages = null;
			Result resultObj = mService.readAndFlagUnreadFromFriend(user.getUid(), friendId);
			messages = (List<ChatMessage>) resultObj.getResultObject();
			//System.out.println(resultObj.getCode());
			//System.out.println(messages);
			if(resultObj.getCode() == Result.SUCCESS && messages != null) {
				JSONArray array = new JSONArray();
				for(ChatMessage msg : messages) {
					array.add(msg);
				}
				out.print(array);
			}
		} else if(action.equals("send")) {
			String content = (String) request.getParameter("content");
			List<ChatMessage> messages = null;
			
			Result resultObj =  uService.getUserById(friendId);
			if(resultObj.getCode() != Result.SUCCESS) return;			
			friend = (User) resultObj.getResultObject();
			
			int uid = user.getUid();
			int result = mService.sendMessage(uid, friendId, content);
			//System.out.println(result);
			if(result == Result.SUCCESS) {
				resultObj = mService.readAndFlagUnreadFromFriend(uid, friendId);
				messages = (List<ChatMessage>) resultObj.getResultObject();
				if(resultObj.getCode() != Result.SUCCESS || messages == null) {
					messages = new ArrayList<ChatMessage>();
				}
				messages.add(new ChatMessage(0, uid, friendId, System.currentTimeMillis(), content));
				JSONArray array = new JSONArray();
				for(ChatMessage msg : messages) {
					array.add(msg);
				}
				out.print(array);
			} else {
				out.print("error");
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
