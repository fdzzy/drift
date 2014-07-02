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
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		String friendIdStr = request.getParameter("friendId");
		String content = request.getParameter("content");
		int uid = 0, friendId = 0;
		try {
			uid = Integer.parseInt(uidStr);
			friendId = Integer.parseInt(friendIdStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 520;
		final int ERR_UNKOWN = 521;
		final int ERR_BAD_ARGS = 522;
		//final int ERR_NO_SUCH_USER = 523;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		int result = DBConnector.sendMessage(uid, friendId, content);

		switch (result) {
		case DBConnector.DB_STATUS_ERR_BAD_ARGS:
			status = ERR_BAD_ARGS;
			map.put("result", "Bad Arguments");
			break;
		case DBConnector.DB_STATUS_OK:
			status = SUCCESS;
			map.put("result", "Succeed");
			break;
		default:
			status = ERR_UNKOWN;
			map.put("result", "Unknown Error");		
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
