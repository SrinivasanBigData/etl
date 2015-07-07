package com.swell.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.swell.mvc.controller.UserController;

public class SecureInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 可以通过的URI
	 */
	private String[] uris;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if(!ArrayUtils.contains(getUris(), request.getServletPath())){
			// 验证用户是否登录
			HttpSession session = request.getSession();
			Object user = session.getAttribute(UserController.SESSION_USER);
			if (user == null) {
				response.sendRedirect(request.getContextPath());
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}

	public String[] getUris() {
		return uris;
	}

	public void setUris(String... uris) {
		this.uris = uris;
	}

}
