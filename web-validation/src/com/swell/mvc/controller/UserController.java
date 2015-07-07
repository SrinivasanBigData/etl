package com.swell.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	private static final String VIEW_INDEX = "index";
	private static final String VIEW_LOGIN = "login";
	public static final String SESSION_USER = "user";

	@RequestMapping("/loginView.do")
	public String loginView(){
		return VIEW_LOGIN;
	}
	
	@RequestMapping("/login.do")
	public String login(HttpServletRequest request,String username,String password){
		// 验证用户名密码
		if(!"s.well".equals(username)){
			return VIEW_LOGIN;
		}
		// 设置session
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER, username);
		
		return VIEW_INDEX;
	}
}
