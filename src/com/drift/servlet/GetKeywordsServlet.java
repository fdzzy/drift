package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drift.bean.User;
import com.drift.sina.WeiboKeywords;
import com.gy.ke.bean.Keyword;

/**
 * Servlet implementation class GetKeywordsServlet
 */
@WebServlet("/get_keywords")
public class GetKeywordsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetKeywordsServlet() {
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
		HttpSession session = request.getSession();
		String accessToken = (String) session.getAttribute(MyServletUtil.SESS_FOREIGN_ACCESS_TOKEN);
		List<Keyword> keywords = null;
		if(accessToken != null) {
			String libraryPath = getServletContext().getRealPath("/");
			System.out.println(libraryPath);
			keywords = WeiboKeywords.getWeiboKeywords(libraryPath, accessToken);
			if(keywords != null) {
				PrintWriter out = response.getWriter();
				out.println("<html><head>");
				out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
				out.println("</head><body>");
				out.println("<center>");
				out.println("<b>key words: </b><br/>");
				out.println("<table>");
				for(Keyword keyword : keywords) {
					//out.println(new String(keyword.getName().getBytes(), "UTF-8") + ": " + keyword.getScore() + "<br/>");
					out.println("<tr>");
					out.println("<td>" + keyword.getName() + ":<td/>\n<td>" + keyword.getScore() + "</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("</center>");
				out.println("</body></html>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
