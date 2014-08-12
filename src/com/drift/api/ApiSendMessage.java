package com.drift.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DBConnector;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiSendMessage
 */
@WebServlet(ApiController.API_ROOT + "/send_message")
public class ApiSendMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiSendMessage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.doCommonTasks(request, response);
		int status = ApiController.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String uidStr = request.getParameter("uid");
		String friendIdStr = request.getParameter("friendId");
		String content = request.getParameter("content");
		if(uidStr == null || friendIdStr == null || content == null) {
			status = ApiController.API_ERR_BAD_ARGS;
		} else {
			int uid = 0, friendId = 0;
			try {
				uid = Integer.parseInt(uidStr);
				friendId = Integer.parseInt(friendIdStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(uid <= 0 || (DBConnector.checkUser(uid) ==  false)) {
				status = ApiController.API_ERR_BAD_USER_ID;
			} else if(friendId <= 0 || (DBConnector.checkUser(friendId) == false)) {
				status = ApiController.API_ERR_BAD_FRIEND_ID;
			} else {
				int rtval = DBConnector.sendMessage(uid, friendId, content);
				status = ApiController.mapDBCode(rtval);
			}
		}
		String msg = ApiController.API_CODE_STRINGS.get(status);
		map.put("code", status);
		map.put("result", msg);
		//System.out.println(status);

		PrintWriter out = response.getWriter();
		out.print(JSONUtil.toJSONString(map));
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
