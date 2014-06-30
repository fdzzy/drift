package com.drift.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.servlet.MyServletUtil;

public class ApiController {
	
	public static final String API_ROOT = "/api";
	
	public static void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		response.setContentType("application/json");
	}

}
