package com.debate.vsys.core.service;

import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public interface UserService {

	public User signup();
	
	public User signup(String secret);
	
	public User signin(String uId);

	public boolean signout(String uId);
	
	public User find(String uId);

	public void addVote(String uId, Vote v);

}
