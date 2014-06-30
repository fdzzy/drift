package com.drift.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.ChatMessage;
import com.drift.core.DBConnector;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiGetUnread
 */
@WebServlet(ApiController.API_ROOT + "/get_unread")
public class ApiGetUnread extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiGetUnread() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		String friendIdStr = request.getParameter("friendId");
		int uid = 0, friendId = 0;
		try {
			uid = Integer.parseInt(uidStr);
			friendId = Integer.parseInt(friendIdStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 51000;
		final int ERR_UNKOWN = 51001;
		//final int ERR_BAD_ARGS = 51002;
		final int ERR_NO_MESSAGES = 51003;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		List<ChatMessage> messages = null;
		messages = DBConnector.getNewMessagesFromFriend(uid, friendId);

		if(messages == null || messages.isEmpty()) {
			status = ERR_NO_MESSAGES;
			map.put("result", "No messages");
		} else {
			status = SUCCESS;
			map.put("result", "Succeed");
			map.put("messages", messages);
		}

		map.put("code", status);
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
