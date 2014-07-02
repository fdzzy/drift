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

import com.drift.core.Bottle;
import com.drift.core.DBConnector;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiGetBottle
 */
@WebServlet(ApiController.API_ROOT + "/get_bottle")
public class ApiGetBottle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiGetBottle() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		int uid = 0;

		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 200;
		final int ERR_UNKOWN = 201;
		final int ERR_BAD_ARGS = 202;
		final int ERR_NO_BOTTLE = 203; 

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		if(uid > 0) {
			Bottle bottle = DBConnector.getBottle(uid);
			if(bottle != null) {
				status = SUCCESS;
				map.put("message", "Succeed");
				map.put("bottleID", bottle.getBottleId());
				map.put("content", bottle.getContent());
				map.put("senderID", bottle.getSenderId());
				map.put("senderName", bottle.getSenderName());
			} else {
				status = ERR_NO_BOTTLE;
				map.put("result", "No Bottle");
			}
		} else {
			status = ERR_BAD_ARGS;
			map.put("result", "Bad Arguments");
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
