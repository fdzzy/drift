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

import com.drift.bean.ChatMessage;
import com.drift.service.MessageService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiGetUnread
 */
@WebServlet(ApiUtil.API_ROOT + "/get_unread")
public class ApiGetUnread extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageService mService = ServiceFactory.createMessageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiGetUnread() {
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
		String friendIdStr = request.getParameter("friendId");
		if(uidStr == null || friendIdStr == null) {
			status = ApiUtil.API_ERR_BAD_ARGS;
		} else {
			int uid = 0, friendId = 0;
			try {
				uid = Integer.parseInt(uidStr);
				friendId = Integer.parseInt(friendIdStr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Result result = mService.readAndFlagUnreadFromFriend(uid, friendId);
			status = ApiUtil.mapCode(result.getCode());

			if(status == ApiUtil.API_ACTION_OK) {
				List<ChatMessage> messages = (List<ChatMessage>) result.getResultObject();
				if(messages == null || messages.isEmpty()) {
					status = ApiUtil.API_ERR_NO_MESSAGE;
				} else {
					map.put("messages", messages);
				}
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
