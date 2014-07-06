package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import com.drift.core.DBConnector;
import com.drift.core.DBResult;
import com.drift.core.User;

/**
 * Servlet implementation class ApiLogin
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);	
		String action = request.getParameter("action");
		String page = MyServletUtil.loginJspPage;
		if(action != null) {
			if(action.equals("login")) {
				page = doLogin(request);				
			}
		}
		getServletContext().getRequestDispatcher(page).forward(request, response);
	}

	private String doLogin(HttpServletRequest request) {
		String page = MyServletUtil.loginJspPage;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		DBResult result = null;
		
		result = DBConnector.login(username, password);
		User user = (User) result.getResultObject();
		
		switch (result.getCode()) {
		case DBConnector.DB_STATUS_OK:
			// login success
			HttpSession session = request.getSession();
			session.setAttribute(MyServletUtil.SESS_USER, user);
			page = MyServletUtil.mainJspPage;
			break;
		case DBConnector.DB_STATUS_ERR_USER_NOT_EXIST:
			request.setAttribute("msg","用户名不存在！");
			break;
		case DBConnector.DB_STATUS_ERR_EMAIL_NOT_EXIST:
			request.setAttribute("msg","邮箱不存在！");
			break;
		case DBConnector.DB_STATUS_ERR_PASSWORD:
			request.setAttribute("msg","密码错误！");
			break;
		case DBConnector.DB_STATUS_ERR_USER_NOT_ACTIVATED:
			request.setAttribute("msg","用户未激活！");
			break;
		case DBConnector.DB_STATUS_ERR_SQL:
			System.err.println("数据库错误！");
		default:
			request.setAttribute("msg","未知错误！");
			break;
		}
		
		return page;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
