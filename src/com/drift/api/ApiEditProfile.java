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
 * Servlet implementation class ApiEditProfile
 */
@WebServlet(ApiController.API_ROOT + "/edit_profile")
public class ApiEditProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiEditProfile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");		//example: '1990-01-01'
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYear = request.getParameter("enrollYear");

		int uid = 0;
		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 400;
		final int ERR_UNKOWN = 401;
		final int ERR_BAD_ARGS = 402;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		int rtval = DBConnector.DB_STATUS_ERR_GENERIC;
		rtval = DBConnector.editProfile(uid, nickname, birthday, school, department, enrollYear, major);

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
