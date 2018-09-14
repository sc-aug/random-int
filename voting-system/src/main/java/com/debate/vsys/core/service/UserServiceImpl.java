package com.debate.vsys.core.service;

import com.debate.vsys.core.data.UserDao;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;
	
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User signup() {
		return this.userDao.signup();
	}
	
	public User signup(String secret) {
		return this.userDao.signup(secret);
	}

	@Override
	public User signin(String uId) {
		return this.userDao.find(uId);
	}

	@Override
	public boolean signout(String uId) {
		return true;
	}
	
	@Override
	public User find(String uId) {
		return this.userDao.find(uId);
	}

	@Override
	public void addVote(String uId, Vote v) {
		this.userDao.addVote(uId, v);
	}
	
}
