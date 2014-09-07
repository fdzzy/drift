package com.drift.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DAO;
import com.drift.core.DBResult;
import com.drift.core.NotificationMessage;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiGetNotification
 */
@WebServlet(ApiController.API_ROOT + "/get_notification")
public class ApiGetNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiGetNotification() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
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

			if(uid <= 0 || (DAO.checkUser(uid) ==  false)) {
				status = ApiController.API_ERR_BAD_USER_ID;
			} else {
				List<NotificationMessage> notifications = null;
				DBResult result = DAO.getNotification(uid);
				status = ApiController.mapDBCode(result.getCode());

				if(status == ApiController.API_ACTION_OK) {
					notifications = (List<NotificationMessage>) result.getResultObject();
					if(notifications == null || notifications.isEmpty()) {
						status = ApiController.API_ERR_NO_MESSAGE;
					} else {
						map.put("messages", notifications);
					}
				}
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
