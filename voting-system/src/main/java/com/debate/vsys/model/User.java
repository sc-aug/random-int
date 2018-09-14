package com.debate.vsys.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class User {

	private String uId;
	private Role role;
	private List<Vote> voteHistory = new LinkedList<>();

	public User() {
		this.uId = UUID.randomUUID().toString();
		this.role = Role.USER;
	}
	
	public User(String secret) {
		this.uId = UUID.randomUUID().toString();
		
		if (secret.equals("admin_secret")) {
			this.role = Role.ADMIN;
		} else if (secret.equals("vp_secret")) {
			this.role = Role.VP;
		} else {
			this.role = Role.USER;
		}
	}

	public String getUId() {
		return uId;
	}

	public Role getRole() {
		return role;
	}

	public List<Vote> getVoteHistory() {
		return voteHistory;
	}

	public void setVoteHistory(List<Vote> voteHistory) {
		this.voteHistory = voteHistory;
	}

}
