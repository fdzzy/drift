package com.drift.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.Bottle;
import com.drift.core.DBConnector;
import com.drift.core.User;

/**
 * Servlet implementation class GetBottleServlet
 */
@WebServlet("/get_bottle")
public class GetBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBottleServlet() {
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
		
		Bottle bottle = null;
		try {
			DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
			bottle = dateDB.getBottle(user.getUid());
		}catch(Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("user", user);
		request.setAttribute("bottle", bottle);
		getServletContext().getRequestDispatcher(MyServletUtil.getBottleJspPage).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
