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
import com.drift.util.PhotoUtil;

/**
 * Servlet implementation class ApiUploadPhoto
 */
@WebServlet(ApiController.API_ROOT + "/upload_photo")
public class ApiUploadPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiUploadPhoto() {
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

		final int SUCCESS = 43000;
		final int ERR_UNKOWN = 43001;
		//final int ERR_BAD_ARGS = 43002;
		//final int ERR_NO_SUCH_USER = 43003;

		int status = ERR_UNKOWN;
		Map<String, Object> map = new HashMap<String, Object>();

		PrintWriter out = response.getWriter();
		if(uid <= 0) {
			map.put("code", status);
			out.print(JSONUtil.toJSONString(map));
			out.flush();
			return;
		}

		String path = getServletContext().getRealPath("/photo"); //上传文件目录
		System.out.println(path);
		int dbStatus = PhotoUtil.uploadPhoto(request, path, uid);

		if(dbStatus == DBConnector.DB_STATUS_OK) {
			status = SUCCESS;
			map.put("result", "Succeed");
		} else {
			map.put("result", "Unkown Error");
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
