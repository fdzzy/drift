package com.drift.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.bean.ChatMessage;
import com.drift.bean.User;
import com.drift.service.MessageService;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class ReplyBottleServlet
 */
@WebServlet("/handle_bottle")
public class HandleBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService uService = ServiceFactory.createUserService();
	private MessageService mService = ServiceFactory.createMessageService();
       
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
			handleReply(request, response, user, bottleId);
		} else if(action.equals("sendback")) {
			handleSendback(request, response, bottleId);
		}
	}

	@SuppressWarnings("unchecked")
	private void handleReply(HttpServletRequest request,
			HttpServletResponse response, User user, int bottleId) throws ServletException,
			IOException {			
		Result resultObj = mService.replyBottle(user, bottleId);
		if(resultObj.getCode() == Result.SUCCESS) {
			//System.out.println(senderID);
			List<ChatMessage> messages = null;
			int senderId = (Integer) resultObj.getResultObject();
			resultObj =  mService.readAndFlagConversation(user.getUid(), senderId);
			if(resultObj.getCode() == Result.SUCCESS) {
				messages = (List<ChatMessage>) resultObj.getResultObject();
			}
			User friend = (User) uService.getUserById(senderId).getResultObject(); 
			request.setAttribute("friend", friend);
			request.setAttribute("messages", messages);
			getServletContext().getRequestDispatcher(MyServletUtil.chatJspPage).forward(request, response);
			//getServletContext().getRequestDispatcher("/send_receive?friend=" + senderID).forward(request, response);
		}
	}
	
	private void handleSendback(HttpServletRequest request,
			HttpServletResponse response, int bottleId) throws ServletException,
			IOException {			
		int result = mService.sendBackBottle(bottleId);
		if(result == Result.SUCCESS) {
			getServletContext().getRequestDispatcher(MyServletUtil.mainJspPage).forward(request, response);
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
