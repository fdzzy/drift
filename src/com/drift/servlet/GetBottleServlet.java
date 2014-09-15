package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.bean.Bottle;
import com.drift.bean.User;
import com.drift.service.MessageService;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class GetBottleServlet
 */
@WebServlet("/get_bottle")
public class GetBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService uService = ServiceFactory.createUserService();
	private MessageService mService = ServiceFactory.createMessageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBottleServlet() {
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
		
		Result result = mService.receiveBottle(user); 		
		if(result.getCode() != Result.SUCCESS) {
			PrintWriter out = response.getWriter();
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html;charset=utf-8' />");
			out.println("</head><body><center><p>暂无消息</p><p><a href='main'>返回</a></p></center></body>");
			return;
		}		

		Bottle bottle = (Bottle) result.getResultObject();
		User sender = (User)uService.getUserById(bottle.getSenderId()).getResultObject();
		String photoUrl = uService.getFullPhotoUrl(sender);
		
		request.setAttribute("user", user);
		request.setAttribute("bottle", bottle);
		request.setAttribute("senderName", sender.getUsername());
		request.setAttribute("senderPhoto", photoUrl);
		getServletContext().getRequestDispatcher(MyServletUtil.getBottleJspPage).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
