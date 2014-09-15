package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drift.bean.User;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class ApiLogin
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = ServiceFactory.createUserService();
       
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
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(MyServletUtil.SESS_USER);
		
		String page = MyServletUtil.loginJspPage;
		if(user == null || user.getUid() <= 0) {
			String action = request.getParameter("action");
			if(action != null) {
				if(action.equals("login")) {
					page = doLogin(request);				
				}
			}
		} else {
			page = MyServletUtil.mainJspPage;
		}
		getServletContext().getRequestDispatcher(page).forward(request, response);
	}

	private String doLogin(HttpServletRequest request) {
		String page = MyServletUtil.loginJspPage;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Result result = null;
		
		System.out.println(username);
		result = service.login(username, password);
		
		switch (result.getCode()) {
		case Result.SUCCESS:
			User user = (User) result.getResultObject();
			HttpSession session = request.getSession();
			session.setAttribute(MyServletUtil.SESS_USER, user);
			page = MyServletUtil.mainJspPage;
			break;
		case Result.ERR_USER_NOT_EXIST:
			request.setAttribute("msg","用户名不存在！");
			break;
		case Result.ERR_EMAIL_NOT_EXIST:
			request.setAttribute("msg","邮箱不存在！");
			break;
		case Result.ERR_PASSWORD:
			request.setAttribute("msg","密码错误！");
			break;
		case Result.ERR_USER_NOT_ACTIVATED:
			request.setAttribute("msg","用户未激活！");
			break;
		case Result.ERR_SQL:
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
