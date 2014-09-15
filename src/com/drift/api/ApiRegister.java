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

import com.drift.bean.User;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiRegister
 */
@WebServlet(ApiUtil.API_ROOT + "/register")
public class ApiRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = ServiceFactory.createUserService();
       
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
		ApiUtil.doCommonTasks(request, response);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sexStr = request.getParameter("sex");
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYearStr = request.getParameter("enrollYear");
		String email = request.getParameter("email");

		if(birthday == null || birthday.isEmpty())
			birthday = "1990-01-01";

		int sex = -1;
		try {
			sex = Integer.parseInt(sexStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int enrollYear = 0;		
		try {
			enrollYear = Integer.parseInt(enrollYearStr);
		} catch (Exception e) {
			// This is not an issue
		}

		User user = null;
		int result = Result.ERR_GENERIC;
		try {
			user = new User(username, nickname, sex, school, department,
					major, email, birthday, enrollYear);
		} catch (Exception e) {
			result = Result.ERR_BAD_ARGS;
		}
		if(result != Result.ERR_BAD_ARGS) {
			result = service.register(user, password);
		}
		int status = ApiUtil.mapCode(result);
		String msg = ApiUtil.API_CODE_STRINGS.get(status);
		//System.out.println(result.getCode() + " " + status);

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
