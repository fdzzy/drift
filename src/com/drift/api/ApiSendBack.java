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
		int status = ApiController.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String bidStr = request.getParameter("bid");
		if(bidStr == null) {
			status = ApiController.API_ERR_BAD_ARGS;
		} else {
			int bid = 0;

			try {
				bid = Integer.parseInt(bidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			int rtval = DBConnector.setBottleUnread(bid);
			status = ApiController.mapDBCode(rtval);
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
