package com.shrimp.ykztx.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.shrimp.ykztx.model.User;

public class BaseController {
	protected static final String CURRENT_USER_SESSION_KEY = "loginUser";

	/**
	 * 获取当前登录的用户
	 * 
	 * @param request
	 * @return
	 */
	protected User getCurrentUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(
				CURRENT_USER_SESSION_KEY);
	}
}
