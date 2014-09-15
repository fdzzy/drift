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

import com.drift.bean.NotificationMessage;
import com.drift.service.MessageService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiGetNotification
 */
@WebServlet(ApiUtil.API_ROOT + "/get_notification")
public class ApiGetNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService mService = ServiceFactory.createMessageService();
       
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
		ApiUtil.doCommonTasks(request, response);
		int status = ApiUtil.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String uidStr = request.getParameter("uid");
		if(uidStr == null) {
			status = ApiUtil.API_ERR_BAD_ARGS;
		} else {
			int uid = 0;
			try {
				uid = Integer.parseInt(uidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Result resultObj = mService.getNotifications(uid);
			status = ApiUtil.mapCode(resultObj.getCode());
			
			List<NotificationMessage> notifications = (List<NotificationMessage>) resultObj.getResultObject();
			if(resultObj.getCode() == Result.SUCCESS && notifications != null) {
				map.put("messages", notifications);
			}
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
