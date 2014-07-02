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

import com.drift.core.DBConnector;
import com.drift.util.JSONUtil;

/**
 * Servlet implementation class ApiViewPhoto
 */
@WebServlet(ApiController.API_ROOT + "/view_photo")
public class ApiViewPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiViewPhoto() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApiController.setCharacterEncoding(request, response);
		
		String uidStr = request.getParameter("uid");
		int uid = 0;
		try {
			uid = Integer.parseInt(uidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int SUCCESS = 420;
		final int ERR_UNKOWN = 421;
		//final int ERR_BAD_ARGS = 422;
		final int ERR_NO_SUCH_USER = 423;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		PrintWriter out = response.getWriter();
		if(uid <= 0) {
			map.put("code", status);
			out.print(JSONUtil.toJSONString(map));
			out.flush();
			return;
		}

		String photoUrl = DBConnector.getPhotoUrl(uid);

		if(photoUrl == null) {
			status = ERR_NO_SUCH_USER;
			map.put("result", "User not exist");
		} else {
			status = SUCCESS;
			map.put("result", "Succeed");
			map.put("photoUrl", photoUrl);
		}

		map.put("code", status);
		//System.out.println(status);

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
