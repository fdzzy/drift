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
 * Servlet implementation class ApiReplyBottle
 */
@WebServlet(ApiController.API_ROOT + "/reply_bottle")
public class ApiReplyBottle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiReplyBottle() {
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
		String bidStr = request.getParameter("bid");
		
		if(uidStr == null || uidStr.isEmpty() 
				|| bidStr == null || bidStr.isEmpty()) {
			status = ApiController.API_ERR_BAD_ARGS;
		} else {
			int uid = 0;
			int bid = 0;

			try {
				uid = Integer.parseInt(uidStr);
				bid = Integer.parseInt(bidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			int	senderID = DBConnector.replyBottle(uid, bid);
			if(senderID > 0) {
				status = ApiController.API_ACTION_OK;
					map.put("senderId", senderID);
			} else {
				status = ApiController.mapDBCode(senderID);
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
