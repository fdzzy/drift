package com.drift.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.bean.User;
import com.drift.service.UserService;
import com.drift.service.impl.Result;
import com.drift.service.impl.ServiceFactory;

/**
 * Servlet implementation class EditProfileServlet
 */
@WebServlet("/edit_profile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = ServiceFactory.createUserService();

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
		
		String action = request.getParameter("action");
		if(action != null) {
			if(action.equals("edit")) {
				doEdit(request,response,user);				
			}
		}
		
		// Refresh User in case the photo url is changed
		user = (User) service.getUserById(user.getUid()).getResultObject();
		request.getSession().setAttribute(MyServletUtil.SESS_USER, user);
		request.setAttribute("photoUrl",service.getFullPhotoUrl(user));
		getServletContext().getRequestDispatcher(MyServletUtil.editProfileJspPage).forward(request, response);
	}

	private void doEdit(HttpServletRequest request, HttpServletResponse response, User user) {
		
		String nickname = request.getParameter("nickname");
		String birthday = request.getParameter("birthday");		//example: '1990-01-01'
		String school = request.getParameter("school");
		String department = request.getParameter("department");
		String major = request.getParameter("major");
		String enrollYearStr = request.getParameter("enrollYear");

		int enrollYear = 0;
		try {
			enrollYear = Integer.parseInt(enrollYearStr);
		} catch (Exception e) {
			// nothing big deal
		}
		
		if(nickname == null || birthday == null || school == null || department == null
				|| department == null || major == null) {
			System.err.println("Edit Profile: error input!");
			return;
		}
		
		user.setNickname(nickname);
		user.setBirthday(birthday);
		user.setSchool(school);
		user.setDepartment(department);
		user.setMajor(major);
		user.setEnrollYear(enrollYear);
		
		int result = service.editProfile(user);
		if(result == Result.SUCCESS) {
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
