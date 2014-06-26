package com.drift.baidu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduApiException;
import com.baidu.api.BaiduOAuthException;
import com.baidu.api.store.BaiduCookieStore;
import com.baidu.api.store.BaiduStore;
import com.drift.baidu.constants.BaiduAppConstant;

/**
 * Servlet implementation class OAuthServlet
 */
public class OAuthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String clientId = BaiduAppConstant.CLIENTID;

    private static final String clientSecret = BaiduAppConstant.CLIENTSECRET;

    private static final String redirectUri = BaiduAppConstant.REDIRECTURI;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuthServlet() {
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
        try {
            baidu = new Baidu(clientId, clientSecret, redirectUri, store, request);
            String state = baidu.getState();
            Map<String, String> params = new HashMap<String, String>();
            params.put("state", state);
            String authorizeUrl = baidu.getBaiduOAuth2Service().getAuthorizeUrl(params);
            response.sendRedirect(authorizeUrl);
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
