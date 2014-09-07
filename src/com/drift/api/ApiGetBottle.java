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
import com.drift.core.DAO;
import com.drift.core.DBResult;
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
		ApiController.doCommonTasks(request, response);
		int status = ApiController.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String uidStr = request.getParameter("uid");
		
		if(uidStr == null) {
			status = ApiController.API_ERR_BAD_ARGS;
		} else {
			int uid = 0;

			try {
				uid = Integer.parseInt(uidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Bottle bottle = null;
			DBResult result = DAO.getBottle(uid);
			status = ApiController.mapDBCode(result.getCode());
			if(status == ApiController.API_ACTION_OK) {
				bottle = (Bottle) result.getResultObject();
				map.put("bottleID", bottle.getBottleId());
				map.put("content", bottle.getContent());
				map.put("senderID", bottle.getSenderId());
				map.put("senderName", bottle.getSenderName());
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
