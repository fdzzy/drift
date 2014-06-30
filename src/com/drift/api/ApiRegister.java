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
import com.drift.core.DBResult;
import com.drift.servlet.MyServletUtil;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiRegister
 */
@WebServlet(ApiController.API_ROOT + "/register")
public class ApiRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiRegister() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYear = request.getParameter("enrollYear");
		String email = request.getParameter("email");

		if(birthday == null || birthday.isEmpty())
			birthday = "1990-01-01";

		int status = MyServletUtil.API_CODE_REGISTER_ERR_UNKOWN;
		DBResult result = DBConnector.register(username, nickname, password, sex, birthday, 
				school, department, major, enrollYear, email);
		status = MyServletUtil.getRegisterStatusCode(result.getCode());	
		String msg = MyServletUtil.getRegisterMessage(status);
		//System.out.println(status);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", status);
		map.put("result", msg);
		
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
