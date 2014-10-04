package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.bean.User;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class TestChatServlet
 */
@WebServlet("/test_chat")
public class TestChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService service = ServiceFactory.createUserService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestChatServlet() {
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
		
		String idStr = request.getParameter("id");
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
			// nothing big deal
		}
		if(id <= 0) return;
		
		Result result = service.getUserById(id);
		User targetUser  = (User) result.getResultObject();
		if(result.getCode() != Result.SUCCESS || targetUser == null) {
			return;
		}
		request.setAttribute("id", user);
		request.setAttribute("photoUrl", service.getFullPhotoUrl(targetUser));
		getServletContext().getRequestDispatcher("/WEB-INF/pages/chat2.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
