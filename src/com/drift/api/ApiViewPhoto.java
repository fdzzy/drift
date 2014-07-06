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

			String photoUrl = DBConnector.getPhotoUrl(uid);
			if(photoUrl == null) {
				status = ApiController.API_ERR_BAD_USER_ID;
			} else {
				status = ApiController.API_ACTION_OK;
				map.put("photoUrl", photoUrl);
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
