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
 * Servlet implementation class IframeDemo
 */
public class IframeDemoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String clientId = BaiduAppConstant.CLIENTID;

    private String redirectUri = BaiduAppConstant.REDIRECTURI;

    private String clientSecret = BaiduAppConstant.CLIENTSECRET;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IframeDemoServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BaiduStore store = new BaiduCookieStore(clientId, request, response);
        try {
            Baidu baidu = new Baidu(clientId, clientSecret, redirectUri, store, request);
            User user = baidu.getLoggedInUser();
            String loginUrl = baidu.getLoginUrl();
            String next = "http://127.0.0.1:8080/demosite/iframeDemo/logout";
            String loginOutUrl = baidu.getLogOutUrl(next);
            request.setAttribute("user", user);
            request.setAttribute("loginUrl", loginUrl);
            request.setAttribute("loginOutUrl", loginOutUrl);
            request.getRequestDispatcher("iframeDemo.jsp").forward(request, response);
        } catch (BaiduOAuthException e) {
            logger.debug("BaiduOAuthException ", e);
        } catch (BaiduApiException e) {
            logger.debug("BaiduApiException ", e);
        }
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
