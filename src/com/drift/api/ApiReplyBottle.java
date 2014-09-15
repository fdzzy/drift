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
import com.drift.service.MessageService;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiReplyBottle
 */
@WebServlet(ApiUtil.API_ROOT + "/reply_bottle")
public class ApiReplyBottle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService uService = ServiceFactory.createUserService();
	private MessageService mService = ServiceFactory.createMessageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiReplyBottle() {
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
		String bidStr = request.getParameter("bid");
		
		if(uidStr == null || uidStr.isEmpty() 
				|| bidStr == null || bidStr.isEmpty()) {
			status = ApiUtil.API_ERR_BAD_ARGS;
		} else {
			int uid = 0;
			int bid = 0;

			try {
				uid = Integer.parseInt(uidStr);
				bid = Integer.parseInt(bidStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Result resultObj = uService.getUserById(uid);
			User user = (User) resultObj.getResultObject();
			int senderId = 0;
			if(resultObj.getCode() == Result.SUCCESS && user != null) {
				resultObj = mService.replyBottle(user, bid);
				senderId = (Integer) resultObj.getResultObject();
			}
			
			status = ApiUtil.mapCode(resultObj.getCode());
			if(status == ApiUtil.API_ACTION_OK) {
				map.put("senderId", senderId);
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
