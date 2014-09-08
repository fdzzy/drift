package com.drift.sina;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drift.core.DAO;
import com.drift.core.DBResult;
import com.drift.servlet.MyServletUtil;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

/**
 * Servlet implementation class CallBackServlet
 */
@WebServlet("/sinaCallBack")
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBackServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		
		String code = request.getParameter("code");
		PrintWriter out = response.getWriter();
		
		if(code == null || code.isEmpty()) {
			String error = request.getParameter("error_description");
			if(error == null || error.isEmpty()) 
				error = request.getParameter("error");
			out.println("<html><head>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
			out.println("</head><body>");
			out.println("<font color='red' size='4'>");
			out.println("<center>");
			out.println("<b>错误信息：</b>" + error + "<br/>");
			out.println("请<a href='login'>重新登录</a>");
			out.println("</center>");
			out.println("</font>");
			out.println("</body></html>");
			request.setAttribute("loginCheck", "fail");
			return;
		}
		
		// Get access token
		Oauth oauth = new Oauth();
		AccessToken token = null;
		try{
			token = oauth.getAccessTokenByCode(code);
		} catch (WeiboException e) {
			out.print("Unauthorized access.<br/>Go back and try again.<br/>");
			if(401 == e.getStatusCode()){
				System.err.println("Unable to get the access token.");
			}else{
				e.printStackTrace();
			}
		}
		if(token == null) {
			return;
		} else {
			System.out.println(token);
		}
		
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
			/*
			out.print("f_uid: " + f_uid + "<br/>");
			out.print("Name: " + weiboUser.getName() + "<br/>");
			out.print("ScreenName: " + weiboUser.getScreenName() + "<br/>");
			out.print("Province: " + weiboUser.getProvince() + "<br/>");
			out.print("City: " + weiboUser.getCity() + "<br/>");
			out.print("Location: " + weiboUser.getLocation() + "<br/>");
			out.print("Description: " + weiboUser.getDescription() + "<br/>");
			out.print("Gender: " + weiboUser.getGender() + "<br/>");
			out.print("online status: " + weiboUser.getOnlineStatus() + "<br/>");
			out.print("<img src='" + weiboUser.getAvatarLarge() + "' /><br/>");
			*/
			HttpSession session = request.getSession();
			session.setAttribute(MyServletUtil.SESS_FOREIGN_UID, f_uid);
			session.setAttribute(MyServletUtil.SESS_FOREIGN_ACCESS_TOKEN, accessTokenStr);
			
			DBResult result = DAO.getForeignUser(DAO.USER_TYPE_SINA, f_uid);
			switch (result.getCode()) {
			case DAO.DB_STATUS_ERR_USER_NOT_EXIST:
				//out.print("Weibo user not registered yet! redirect to register page<br/>");
				request.setAttribute("f_uid", f_uid);
				request.setAttribute("Name", weiboUser.getName());
				request.setAttribute("ScreenName", weiboUser.getScreenName());
				request.setAttribute("Gender", weiboUser.getGender());
				request.setAttribute("imgUrl", weiboUser.getAvatarLarge());
				getServletContext().getRequestDispatcher("/third_party_register.jsp").forward(request, response);
				break;
			case DAO.DB_STATUS_ERR_USER_NOT_ACTIVATED:
				request.setAttribute("msg", "24小时试用期已过，请去邮箱激活");
				getServletContext().getRequestDispatcher(MyServletUtil.loginJspPage).forward(request, response);
				break;
			case DAO.DB_STATUS_OK:
				com.drift.core.User user = (com.drift.core.User) result.getResultObject();
				session.setAttribute(MyServletUtil.SESS_USER, user);
				/*int rtval = DAO.updateForeignAccessToken(user.getUid(), accessTokenStr);
				if(rtval != DAO.DB_STATUS_OK) {
					System.err.println("Update access token failed!");
				}*/
				//getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);
				response.sendRedirect(getServletContext().getContextPath() + MyServletUtil.mainJspPage);
			default:
				break;
			}
			/*
			 * rtval = loginForeignUser(type, f_uid);
			 * switch(rtval) {
			 * case NOT_EXIST:
			 *   register user;
			 *   break;
			 * case NOT_ACTIVATED:
			 *   ask user to activate;
			 *   break;
			 * case OK:
			 *   login user;
			 *   break;
			 * }
			 *
			 */
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
