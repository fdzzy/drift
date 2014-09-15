package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.bean.ChatMessage;
import com.drift.bean.User;
import com.drift.service.MessageService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class MyBottlesServlet
 */
@WebServlet("/my_bottle")
public class MyBottlesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService mService = ServiceFactory.createMessageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyBottlesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		
		User user = MyServletUtil.checkLogin(request, response);
		if(user == null) {
			return;
		}		
		
		//Set<User> friends = null;
		List<ChatMessage> messages = null;
		//Result result = DAO.getMyBottles(user.getUid(), 0, 30);
		Result result = mService.getMyBottles(user, 0, 30);
		if(result.getCode() == Result.SUCCESS) {
			messages = (List<ChatMessage>) result.getResultObject();
		}
		
		if(messages != null && !messages.isEmpty()) {
			request.setAttribute("messages", messages);
			//System.out.println(friends);
			getServletContext().getRequestDispatcher(MyServletUtil.messagesJspPage).forward(request, response);
		} else {
			PrintWriter out = response.getWriter();
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html;charset=utf-8' />");
			out.println("</head><body><center><p>暂无消息</p><p><a href='main'>返回</a></p></center></body>");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
