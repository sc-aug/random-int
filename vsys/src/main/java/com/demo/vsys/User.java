package com.demo.vsys;

import java.util.HashSet;
import java.util.Set;

import com.demo.vsys.enums.Role;

public class User {
	
	private int uId;
	private Role role;
	private Set<Vote> votes = new HashSet<>();

	public User(int id, Role r) {
		this.uId = id;
		this.role = r;
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

}
