package com.drift.baidu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduApiException;
import com.baidu.api.BaiduOAuthException;
import com.baidu.api.domain.User;
import com.baidu.api.store.BaiduCookieStore;
import com.baidu.api.store.BaiduStore;
import com.drift.baidu.constants.BaiduAppConstant;

/**
 * Servlet implementation class CallBackServlet
 */
public class CallBackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String clientId = BaiduAppConstant.CLIENTID;

    private String redirectUri = BaiduAppConstant.REDIRECTURI;

    private String clientSecret = BaiduAppConstant.CLIENTSECRET;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBackServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BaiduStore store = new BaiduCookieStore(clientId, request, response);
        Baidu baidu = null;
        String accessToken = "";
        String refreshToken = "";
        String sessionKey = "";
        String sessionSecret = "";
        User loggedInUser = null;
        try {
            baidu = new Baidu(clientId, clientSecret, redirectUri, store, request);
            accessToken = baidu.getAccessToken();
            refreshToken = baidu.getRefreshToken();
            sessionKey = baidu.getSessionKey();
            sessionSecret = baidu.getSessionSecret();
            loggedInUser = baidu.getLoggedInUser();
        } catch (BaiduApiException e) {
            logger.debug("BaiduApiException", e);
        } catch (BaiduOAuthException e) {
            logger.debug("BaiduOAuthException ", e);
        }
        request.setAttribute("accessToken", accessToken);
        request.setAttribute("refreshToken", refreshToken);
        request.setAttribute("sessionKey", sessionKey);
        request.setAttribute("sessionSecret", sessionSecret);
        if (loggedInUser != null) {
            request.setAttribute("user", loggedInUser);
        }
        request.getRequestDispatcher("baiduAccesstoken.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
