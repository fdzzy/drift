package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.drift.core.ChatMessage;
import com.drift.core.DBConnector;
import com.drift.core.DBResult;
import com.drift.core.User;

/**
 * Servlet implementation class SendReceiveMessage
 */
@WebServlet(urlPatterns = {"/send_receive"}, asyncSupported=true)
public class SendReceiveMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
			getServletContext().getRequestDispatcher("errorpage.jsp").forward(request, response);
			return;
		}
		User friend = null;
		if(action == null) {
			List<ChatMessage> messages = null;
			DBResult result = DBConnector.getConversation(user.getUid(), friendId);
			if(result.getCode() == DBConnector.DB_STATUS_OK) {
				messages = (List<ChatMessage>) result.getResultObject();
			}
			friend = DBConnector.getUser(friendId);
			
			request.setAttribute("friend", friend);
			request.setAttribute("messages", messages);
			getServletContext().getRequestDispatcher("/chat.jsp").forward(request, response);
		} else if(action.equals("receive")) {
			List<ChatMessage> messages = null;
			DBResult result = DBConnector.getNewMessagesFromFriend(user.getUid(), friendId);
			if(result.getCode() == DBConnector.DB_STATUS_OK) {
				messages = (List<ChatMessage>) result.getResultObject();
			}
			
			JSONArray array = new JSONArray();
			for(ChatMessage msg : messages) {
				array.add(msg);
			}
			out.print(array);
		} else if(action.equals("send")) {
			String content = (String) request.getParameter("content");
			List<ChatMessage> messages = null;
			int uid = user.getUid();
			int rtval = DBConnector.sendMessage(uid, friendId, content);
			if(rtval == DBConnector.DB_STATUS_OK) {
				DBResult result = DBConnector.getNewMessagesFromFriend(uid, friendId);
				if(result.getCode() == DBConnector.DB_STATUS_OK) {
					messages = (List<ChatMessage>) result.getResultObject();
				}
				messages.add(new ChatMessage(0, uid, friendId, new Timestamp(System.currentTimeMillis()), content));
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
