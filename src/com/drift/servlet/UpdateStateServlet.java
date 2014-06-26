package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.drift.core.DBConnector;
import com.drift.core.User;

/**
 * Servlet implementation class UpdateStateServlet
 */
@WebServlet("/update_state")
public class UpdateStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private static Set<User> onlineUsers = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateStateServlet() {
        super();
        //onlineUsers = new HashSet<User>();
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
		
		//onlineUsers.add(user);
		int messageCount = 0;
		try {
			DBConnector dateDB = MyServletUtil.getDateDB(getServletContext());
			messageCount = dateDB.getNewMessageCount(user.getUid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("newMessageCount", messageCount);
		PrintWriter out = response.getWriter();
		out.print(obj);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
