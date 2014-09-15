package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class CheckUsernameServlet
 */
@WebServlet("/check_username")
public class CheckUsernameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUsernameServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MyServletUtil.setCharacterEncoding(request, response);
		String username = request.getParameter("username");
		/**
		 * TODO: need some further investigation here, although we called requset.setCharacterEncoding
		 * we still need the following line to get the correct character
		 * Answer: Seems the setCharacterEncoding is only effective for POST method
		 */
		username = new String(username.getBytes("ISO8859-1"), "UTF-8");
		//System.out.println(username);
		PrintWriter out = response.getWriter();
		
		if(username == null || username.isEmpty()) {
			out.print("false");
			return;
		}		
		
		int result = ServiceFactory.createUserService().checkUsername(username);		
		if(result == Result.SUCCESS) {
			out.print("true");
		} else {
			out.print("false");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
