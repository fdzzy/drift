package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.Bottle;
import com.drift.core.DAO;
import com.drift.core.DBResult;
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
		
		Bottle bottle = null;
		DBResult result = DAO.getBottle(user.getUid());
		
		if(result.getCode() == DAO.DB_STATUS_OK) {
			bottle = (Bottle) result.getResultObject();
		}
		
		request.setAttribute("user", user);
		request.setAttribute("bottle", bottle);
		getServletContext().getRequestDispatcher(MyServletUtil.getBottleJspPage).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
