package com.debate.vsys.cli;

import com.debate.vsys.model.User;

public class CliContext {
	
	private static User currentUser;
	
	public void setCurrentUser(User u) {
		currentUser = u;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
}
