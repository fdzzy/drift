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

import com.drift.core.DAO;
import com.drift.core.DBResult;
import com.drift.core.User;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiLogin
 */
@WebServlet(ApiController.API_ROOT + "/login")
public class ApiLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		ApiController.doCommonTasks(request, response);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		int status = ApiController.API_ERR_OTHER;
		DBResult result = DAO.login(username, password);
		status = ApiController.mapDBCode(result.getCode());
		String msg = ApiController.API_CODE_STRINGS.get(status);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", status);
		map.put("result", msg);
		if(status == ApiController.API_ACTION_OK) {
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
