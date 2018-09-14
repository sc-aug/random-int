package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.model.Role;
import com.debate.vsys.model.User;

public class Cmd {
	
	public static Role getRole(CliContext cliCtx) {
		User u = cliCtx.getCurrentUser();
		return u == null ? null : u.getRole();
	}
	
	public static String str(String format, Object... args) {
		return String.format(format, args);
	}
	
}
