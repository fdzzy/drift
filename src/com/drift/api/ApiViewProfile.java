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
import com.drift.core.User;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiViewProfile
 */
@WebServlet(ApiController.API_ROOT + "/view_profile")
public class ApiViewProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiViewProfile() {
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

		final int SUCCESS = 410;
		final int ERR_UNKOWN = 411;
		//final int ERR_BAD_ARGS = 412;
		final int ERR_NO_SUCH_USER = 413;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		User targetUser = DBConnector.getUser(uid);

		if(targetUser == null) {
			status = ERR_NO_SUCH_USER;
			map.put("result", "User does not exist");
		} else {
			status = SUCCESS;
			map.put("result", "Succeed");
			map.put("profile", targetUser);
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
