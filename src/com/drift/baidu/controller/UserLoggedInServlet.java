package com.drift.baidu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.api.BaiduApiClient;
import com.baidu.api.BaiduApiException;
import com.baidu.api.domain.Session;
import com.baidu.api.domain.User;
import com.baidu.api.service.IUserService;
import com.baidu.api.service.impl.UserServiceImpl;
import com.baidu.api.store.BaiduCookieStore;
import com.baidu.api.store.BaiduStore;
import com.drift.baidu.constants.BaiduAppConstant;

/**
 * Servlet implementation class UserLoggedInServlet
 */
public class UserLoggedInServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoggedInServlet() {
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
        User loggedInUser= null;
        try {
            loggedInUser = userService.getLoggedInUser();
        } catch (BaiduApiException e) {
            
        }
        request.setAttribute("result", loggedInUser);
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
