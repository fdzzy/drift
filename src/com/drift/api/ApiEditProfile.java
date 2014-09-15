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
 * Servlet implementation class ApiEditProfile
 */
@WebServlet(ApiUtil.API_ROOT + "/edit_profile")
public class ApiEditProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = ServiceFactory.createUserService();
       
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
		ApiUtil.doCommonTasks(request, response);
		int status = ApiUtil.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String uidStr = request.getParameter("uid");
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");		//example: '1990-01-01'
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYearStr = request.getParameter("enrollYear");

		if(uidStr == null) {
			status = ApiUtil.API_ERR_BAD_ARGS;
		} else {
			int uid = 0;
			
			try {
				uid = Integer.parseInt(uidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int enrollYear = 0;
			try {
				enrollYear = Integer.parseInt(enrollYearStr);
			} catch (Exception e) {
				// nothing big deal
			}
			
			Result resultObj = service.getUserById(uid);
			int result = resultObj.getCode();
			User user = (User) resultObj.getResultObject();
			if(result == Result.SUCCESS && user != null) {
				user.setNickname(nickname);
				user.setBirthday(birthday);
				user.setSchool(school);
				user.setDepartment(department);
				user.setMajor(major);
				user.setEnrollYear(enrollYear);
				result = service.editProfile(user);
			}
			
			status = ApiUtil.mapCode(result);
		}
		String msg = ApiUtil.API_CODE_STRINGS.get(status);
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
