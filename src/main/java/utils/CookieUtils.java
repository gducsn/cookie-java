package utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	public static Cookie getCookie(HttpServletRequest httpServletRequest, String cookieKey) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies == null) {
			return null;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(cookieKey)) {
				return cookie;
			}
		}

		return null;
	}

	public static void addCookie(HttpServletResponse httpServletResponse, int cookieExpiration, String cookieKey,
			String cookieValue) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(cookieExpiration);
		httpServletResponse.addCookie(cookie);
	}

}
