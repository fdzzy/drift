package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DBConnector;

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
		System.out.println(username);
		PrintWriter out = response.getWriter();
		
		if(username == null || username.isEmpty()) {
			out.print("false");
			return;
		}		
		
		int result = DBConnector.DB_STATUS_ERR_GENERIC;
		result = DBConnector.checkUsername(username);
		
		if(result == DBConnector.DB_STATUS_OK) {
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
