package com.shrimp.ykztx.model.dao.impl;

import org.springframework.stereotype.Repository;

import com.shrimp.ykztx.model.User;
import com.shrimp.ykztx.model.dao.UserDAO;

@Repository
public class UserDAOImpl extends DefaultBaseDAO<User, String> implements UserDAO  {


	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public User getByLoginName(String loginName) {
		return getByProperty("loginName", loginName);
	}

	@Override
	public User getByEmail(String email) {
		return getByProperty("email", email);
	}

}
