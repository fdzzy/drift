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

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiForeignLogin
 */
@WebServlet(ApiUtil.API_ROOT + "/foreign_login")
public class ApiForeignLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiForeignLogin() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiUtil.doCommonTasks(request, response);
		int status = ApiUtil.API_ERR_OTHER;
		Map<String, Object> map = new HashMap<String, Object>();
		
		String code = request.getParameter("code");		
		String description = null;		
		
		if(code == null || code.isEmpty()) {
			status = ApiUtil.API_ERR_BAD_ARGS;
		} else {
			// Get access token
			Oauth oauth = new Oauth();
			AccessToken token = null;
			try{
				token = oauth.getAccessTokenByCode(code);
			} catch (WeiboException e) {
				description = "Unauthorized access";
				if(401 == e.getStatusCode()){
					description = "Unable to get the access token";
				}else{
					e.printStackTrace();
					description = e.getError();
				}
			}
			if(token == null) {
				//description = "Token can't be get by code";
			} else {
				System.out.println(token);
				
				// Get user info
				String accessTokenStr = token.getAccessToken();
				String f_uid = token.getUid();
				Users um = new Users();
				um.client.setToken(accessTokenStr);
				weibo4j.model.User weiboUser = null;				
				try {
					weiboUser = um.showUserById(f_uid);
					//Log.logInfo(user.toString());
					System.out.println(weiboUser.toString());
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				if(weiboUser != null) {
					map.put("foreignUid", f_uid);
					map.put("accessTokenStr", accessTokenStr);
					status = ApiUtil.API_ACTION_OK;
				}
			}
		}
			
		String msg = ApiUtil.API_CODE_STRINGS.get(status);
		map.put("code", status);
		map.put("result", msg);
		if(description != null && !description.isEmpty()) {
			map.put("description", description);
		}
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
