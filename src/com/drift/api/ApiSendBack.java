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
 * Servlet implementation class ApiSendBack
 */
@WebServlet(ApiController.API_ROOT + "/send_back")
public class ApiSendBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiSendBack() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String bidStr = request.getParameter("bid");
		int bid = 0;

		try {
			bid = Integer.parseInt(bidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 21000;
		final int ERR_UNKOWN = 21001;
		final int ERR_BAD_ARGS = 21002;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		int result = DBConnector.setBottleUnread(bid);
		switch(result) {
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
