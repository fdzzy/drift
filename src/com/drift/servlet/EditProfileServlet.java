package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.core.DAO;
import com.drift.core.User;

/**
 * Servlet implementation class EditProfileServlet
 */
@WebServlet("/edit_profile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		
		User user = MyServletUtil.checkLogin(request, response);
		if(user == null) {
			return;
		}
		
		int uid = user.getUid();
		String action = request.getParameter("action");
		if(action != null) {
			if(action.equals("edit")) {
				doEdit(request,response,uid);				
			}
		}
		
		String photoUrl = DAO.getPhotoUrl(uid);
		request.setAttribute("photoUrl",photoUrl);
		getServletContext().getRequestDispatcher(MyServletUtil.editProfileJspPage).forward(request, response);
	}

	private void doEdit(HttpServletRequest request, HttpServletResponse response, int uid) {
		
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");		//example: '1990-01-01'
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYear = request.getParameter("enrollYear");
		if(enrollYear==null || enrollYear.isEmpty())
			enrollYear = 0 + "";
		
		if(nickname == null || birthday == null || school == null || department == null
				|| department == null || major == null) {
			System.err.println("Edit Profile: null input!");
			return;
		}
		
		int rtval = DAO.DB_STATUS_ERR_GENERIC;
		rtval = DAO.editProfile(uid, nickname, birthday, school, department, enrollYear, major);
		if(rtval == DAO.DB_STATUS_OK) {
			String msg = "<center><p><font color='green'>修改成功！</font></p></center>";
			request.setAttribute("msg",msg);
		} else {
			String msg = "<center><p><font color='red'>修改失败！</font></p></center>";
			request.setAttribute("msg",msg);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
