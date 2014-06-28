package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DBConnector;
import com.drift.core.User;

/**
 * Servlet implementation class PostBottleServlet
 */
@WebServlet("/do_post")
public class PostBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostBottleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MyServletUtil.setCharacterEncoding(request, response);
		
		User user = MyServletUtil.checkLogin(request, response);
		if(user == null) {
			return;
		}
		
		String content = request.getParameter("content");
		System.out.println("\"" + user.getUsername() + "\"" + "发送了消息：\"" + content + "\"");
		
		int rtval = 0;
		try {
			DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
			rtval = dateDB.postBottle(user.getUid(), content);
		}catch(Exception e) {
			e.printStackTrace();
		}
		String msg;
		switch (rtval) {
		case DBConnector.DB_STATUS_OK:
			msg = "<font color='green'>发送成功！</font>";
			break;
		default:
			msg = "<font color='red'>发送失败，请重试！</font>";
			break;
		}
		//System.out.println(msg);
		request.setAttribute("msg", msg);
		getServletContext().getRequestDispatcher(MyServletUtil.doPostBottleJspPage).forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
