package com.drift.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.ChatMessage;
import com.drift.core.DBConnector;
import com.drift.core.User;

/**
 * Servlet implementation class ReplyBottleServlet
 */
@WebServlet("/handle_bottle")
public class HandleBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleBottleServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		User user = MyServletUtil.checkLogin(request, response);
		if(user == null) {
			return;
		}
		
		String action = (String)request.getParameter("action");
		if(action == null || action.isEmpty()) {
			return;
		}
		
		int bottleId = Integer.parseInt((String)request.getParameter("bid"));
		//System.out.println(bottleId);
		if(bottleId <= 0) {
			System.err.println("bid is " + bottleId);
			return;
		}
		
		if(action.equals("reply")) {
			handleReply(request, response, user.getUid(), bottleId);
		} else if(action.equals("sendback")) {
			handleSendback(request, response, bottleId);
		}
	}

	private void handleReply(HttpServletRequest request,
			HttpServletResponse response, int uid, int bottleId) throws ServletException,
			IOException {			
		int senderID = DBConnector.DB_STATUS_ERR_GENERIC;
		
		senderID = DBConnector.replyBottle(uid, bottleId);
		if(senderID > 0) {
			//System.out.println(senderID);
			List<ChatMessage> messages = null;
			User friend = null;
			messages = DBConnector.getConversation(uid, senderID);
			friend = DBConnector.getUser(senderID);
			request.setAttribute("friend", friend);
			request.setAttribute("messages", messages);
			getServletContext().getRequestDispatcher("/chat.jsp").forward(request, response);
			//getServletContext().getRequestDispatcher("/send_receive?friend=" + senderID).forward(request, response);
		}
	}
	
	private void handleSendback(HttpServletRequest request,
			HttpServletResponse response, int bottleId) throws ServletException,
			IOException {			
		int result = DBConnector.DB_STATUS_ERR_GENERIC;
		result = DBConnector.setBottleUnread(bottleId);
		if(result == DBConnector.DB_STATUS_OK) {
			getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
