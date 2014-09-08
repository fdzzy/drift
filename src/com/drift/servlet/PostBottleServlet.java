package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DAO;
import com.drift.core.User;

/**
 * Servlet implementation class PostBottleServlet
 */
@WebServlet("/post_bottle")
public class PostBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostBottleServlet() {
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
		
		String page = MyServletUtil.postBottleJspPage;
		String action = request.getParameter("action");
		if(action != null && action.equals("do_post")) {
			String content = request.getParameter("content");
			System.out.println("\"" + user.getUsername() + "\"" + "发送了消息：\"" + content + "\"");
			
			int rtval = 0;
			rtval = DAO.postBottle(user.getUid(), content);
			
			String msg;
			switch (rtval) {
			case DAO.DB_STATUS_OK:
				msg = "<font color='green'>发送成功！</font>";
				break;
			default:
				msg = "<font color='red'>发送失败，请重试！</font>";
				break;
			}
			//System.out.println(msg);
			request.setAttribute("msg", msg);
			page = MyServletUtil.doPostBottleJspPage;
		}		
		getServletContext().getRequestDispatcher(page).forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
