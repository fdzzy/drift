package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.drift.bean.User;
import com.drift.service.MessageService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class UpdateStateServlet
 */
@WebServlet("/update_state")
public class UpdateStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService mService = ServiceFactory.createMessageService();
	
	//private static Set<User> onlineUsers = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateStateServlet() {
        super();
        //onlineUsers = new HashSet<User>();
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
		
		//onlineUsers.add(user);
		int messageCount = 0;
		Result resultObj = mService.getNewMessageCount(user.getUid());
		if(resultObj.getCode() == Result.SUCCESS) {
			messageCount = (Integer) resultObj.getResultObject();
		}
		//System.out.println(messageCount);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newMessageCount", messageCount);
		JSONObject obj = new JSONObject(map);
		PrintWriter out = response.getWriter();
		out.print(obj);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
