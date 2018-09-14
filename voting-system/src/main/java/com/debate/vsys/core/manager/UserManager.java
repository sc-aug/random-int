package com.debate.vsys.core.manager;

import com.debate.vsys.core.service.UserService;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public class UserManager {
	
	private UserService userService;
	
	public UserManager(UserService us) {
		this.userService = us;
	}
	
	/**
	 * By default a new user will automatically signup
	 * @return User
	 */
	public User signup() {
		return this.userService.signup();
	}
	
	public User signup(String secret) {
		return this.userService.signup(secret);
	}
	
	/**
	 * uId is the only parameter needed for signin. so keep uId secret.
	 * @param uId
	 * @return User
	 */
	public User signin(String uId) {
		return this.userService.signin(uId);
	}
	
	/**
	 * signout base on uId
	 * @param uId
	 * @return boolean
	 */
	public boolean signout(String uId) {
		return this.userService.signout(uId);
	}
	
	/**
	 * Fetch user by uId
	 * @return User
	 */
	public User find(String uId) {
		return this.userService.find(uId);
	}
	
	/**
	 * add vote history
	 */
	public void addVote(String uId, Vote v) {
		this.userService.addVote(uId, v);
	}

}
