package cookie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.CookieUtils;

@WebServlet("/insert")
public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CookieServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String uservalue = request.getParameter("user");

		CookieUtils.addCookie(response, 60, "usercookie", uservalue);

		response.sendRedirect(request.getContextPath() + "/result");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
