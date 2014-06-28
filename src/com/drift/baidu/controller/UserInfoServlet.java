package com.drift.baidu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.api.BaiduApiClient;
import com.baidu.api.BaiduApiException;
import com.baidu.api.domain.Session;
import com.baidu.api.service.IUserService;
import com.baidu.api.service.impl.UserServiceImpl;
import com.baidu.api.store.BaiduCookieStore;
import com.baidu.api.store.BaiduStore;
import com.drift.baidu.constants.BaiduAppConstant;

/**
 * Servlet implementation class UserInfoServlet
 */
public class UserInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInfoServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getAccessToken(response, request);
        IUserService userService = new UserServiceImpl(new BaiduApiClient(token));
        long uid = 0;
        String userParameter = request.getParameter("uid");
        if (userParameter != null && !userParameter.trim().equals("")) {
            uid = Long.valueOf(userParameter);
        }
        String fields = request.getParameter("fields");
        String[] split = fields.split("\\,");
        String info = "";
        try {
            info = userService.getInfo(uid, split);
        } catch (BaiduApiException e) {
            info = e.toString();
        }
        request.setAttribute("result", info);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String getAccessToken(HttpServletResponse response, HttpServletRequest request) {
        BaiduStore store = new BaiduCookieStore(BaiduAppConstant.CLIENTID, request, response);
        Session session = store.getSession();
        if (session == null) {
            try {
                response.sendRedirect("index");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return session.getToken().getAccessToken();

    }

}
