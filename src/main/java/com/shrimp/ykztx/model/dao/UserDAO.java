package com.shrimp.ykztx.model.dao;

import com.shrimp.ykztx.model.User;

public interface UserDAO extends BaseDAO<User, String> {
	
	/**
	 * 根据用户名查找用户
	 * @param loginName 用户名
	 * @return
	 */
	User getByLoginName(String loginName);
	
	/**
	 * 根据邮箱账号查找用户
	 * @param email 邮箱账号
	 * @return
	 */
	User getByEmail(String email);
}
