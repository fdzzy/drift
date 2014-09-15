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
 * Servlet implementation class ApiLogin
 */
@WebServlet(ApiUtil.API_ROOT + "/login")
public class ApiLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = ServiceFactory.createUserService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiLogin() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiUtil.doCommonTasks(request, response);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		int status = ApiUtil.API_ERR_OTHER;
		Result result = service.login(username, password);
		status = ApiUtil.mapCode(result.getCode());
		String msg = ApiUtil.API_CODE_STRINGS.get(status);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", status);
		map.put("result", msg);
		if(status == ApiUtil.API_ACTION_OK) {
			User user = (User) result.getResultObject();
			map.put("uid", user.getUid());
		}
		
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
