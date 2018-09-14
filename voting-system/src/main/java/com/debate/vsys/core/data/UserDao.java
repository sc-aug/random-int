package com.debate.vsys.core.data;

import java.util.concurrent.ConcurrentHashMap;

import com.debate.vsys.core.config.CoreResources;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

/**
 * User info storage
 */
public class UserDao {
	
	/**
	 * Store all the user data
	 */
	private static ConcurrentHashMap<String, User> usersDb;
	
	public UserDao(CoreResources res) {
		usersDb = res.getUsersDb();
	}
	
	public User signup() {
		User u = new User();
		usersDb.put(u.getUId(), u);
		return u;
	}
	
	public User signup(String secret) {
		User u = new User(secret);
		usersDb.put(u.getUId(), u);
		return u;
	}
	
	public User find(String uId) {
		return usersDb.get(uId);
	}
	
	public void addVote(String uId, Vote v) {
		usersDb.get(uId).getVoteHistory().add(v);
	}
	
}
