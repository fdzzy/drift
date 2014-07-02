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
 * Servlet implementation class ApiSendBottle
 */
@WebServlet(ApiController.API_ROOT + "/send_bottle")
public class ApiSendBottle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiSendBottle() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		String content = request.getParameter("content");
		int uid = 0;

		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 220;
		final int ERR_UNKOWN = 221;
		final int ERR_BAD_ARGS = 222;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		int	rtval = DBConnector.postBottle(uid, content);
		switch (rtval) {
		case DBConnector.DB_STATUS_OK:
			status = SUCCESS;
			map.put("result", "Succeed");
			break;
		case DBConnector.DB_STATUS_ERR_BAD_ARGS:
			status = ERR_BAD_ARGS;
			map.put("result", "Bad Arguments");
			break;
		default:
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
