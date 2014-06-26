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

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		String action = request.getParameter("action");
		String page = MyServletUtil.registerJspPage;
		if(action != null) {
			if(action.equals("register")) {
				page = doRegister(request);
			} else if (action.equals("activate")) {
				page = doActivate(request);
			}
		}
		getServletContext().getRequestDispatcher(page).forward(request, response);
	}

	private String doActivate(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String page = MyServletUtil.activateJspPage;
		
		// Get all parameters
		String email = request.getParameter("email");
		String activateCode = request.getParameter("activateCode");
		
		if(email == null || email.isEmpty() 
				|| activateCode == null || activateCode.isEmpty()) {
			request.setAttribute("msg", "激活错误！");
			return page;
		}
		
		int result = DBConnector.DB_STATUS_ERR_GENERIC;
		try {
			DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
			result = dateDB.checkActivate(email, activateCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		switch (result) {
		case DBConnector.DB_STATUS_OK:
			request.setAttribute("msg","<font color='green'>激活成功</font><br/><br/>"
					+ "<a href='" + MyServletUtil.entryURL + "'>点此登录</a>");
			break;

		default:
			request.setAttribute("msg","<font color='red'>激活失败！</font>");
			break;
		}
		
		return page;
	}

	/*
	 * Return value: the page to forward to
	 */
	private String doRegister(HttpServletRequest request) {
		String page = MyServletUtil.registerJspPage;
		
		// Get all parameters
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String birthday = request.getParameter("birthday");		//example: '1990-01-01'
		String username = request.getParameter("username");
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String email = request.getParameter("email");
		String enrollYear = request.getParameter("enrollYear");
		
		if(enrollYear==null || enrollYear.isEmpty())
			enrollYear = 0 + "";
		
		if(username == null || username.isEmpty() ||
		   password == null || password.isEmpty() || 
		   sex == null || sex.isEmpty() ||
		   email == null || email.isEmpty()) {
			request.setAttribute("msg","用户名、密码、性别、邮箱不能为空！");
			return page;
		}
		
		DBResult result = null;
		try {
			DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
			result = dateDB.register(username, nickname, password, sex, birthday, school, 
						department, major, enrollYear, email);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		switch (result.getCode()) {
		case DBConnector.DB_STATUS_OK:
			page = MyServletUtil.registerOkJspPage;
			request.setAttribute("username",username);
			request.setAttribute("email", email);
			break;
		case DBConnector.DB_STATUS_ERR_USER_EXISTS:
			request.setAttribute("msg","用户名已存在！");
			break;
		case DBConnector.DB_STATUS_ERR_EMAIL_EXISTS:
			request.setAttribute("msg","邮箱已被注册！");
			break;
		case DBConnector.DB_STATUS_ERR_SQL:	
			System.err.println("数据库错误");
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
