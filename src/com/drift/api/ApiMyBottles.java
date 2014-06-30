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
 * Servlet implementation class ApiMyBottles
 */
@WebServlet(ApiController.API_ROOT + "/my_bottles")
public class ApiMyBottles extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiMyBottles() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		//String content = request.getParameter("content");
		int uid = 0;

		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 30000;
		final int ERR_UNKOWN = 30001;
		final int ERR_BAD_ARGS = 30002;
		final int ERR_NO_BOTTLES = 30003;	

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		PrintWriter out = response.getWriter();
		if(uid <= 0) {
			status = ERR_BAD_ARGS;
			map.put("result", "Bad Arguments");
			map.put("code", status);
			out.print(JSONUtil.toJSONString(map));
			out.flush();
			return;
		}

		List<ChatMessage> messages = DBConnector.getMyBottles(uid, 0, 30);

		if (messages == null || messages.isEmpty()) {
			status = ERR_NO_BOTTLES;
			map.put("result", "No bottles");
		} else {
			status = SUCCESS;
			map.put("result", "Succeed");
			map.put("bottles", messages);
		}
		map.put("code", status);
		//System.out.println(status);

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
