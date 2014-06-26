package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.drift.core.ChatMessage;
import com.drift.core.DBConnector;
import com.drift.core.User;

/**
 * Servlet implementation class SendReceiveMessage
 */
@WebServlet("/send_receive")
public class SendReceiveMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendReceiveMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			try {
				DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
				messages = dateDB.getConversation(user.getUid(), friendId);
				friend = dateDB.getUser(friendId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("friend", friend);
			request.setAttribute("messages", messages);
			getServletContext().getRequestDispatcher("/chat.jsp").forward(request, response);
		} else if(action.equals("receive")) {
			List<ChatMessage> messages = null;
			try {
				DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
				messages = dateDB.getNewMessagesFromFriend(user.getUid(), friendId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONArray array = new JSONArray();
			for(ChatMessage msg : messages) {
				array.add(msg);
			}
			out.print(array);
		} else if(action.equals("send")) {
			String content = (String) request.getParameter("content");
			List<ChatMessage> messages = null;
			try {
				DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
				messages = dateDB.sendMessage(user.getUid(), friendId, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONArray array = new JSONArray();
			for(ChatMessage msg : messages) {
				array.add(msg);
			}
			out.print(array);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
