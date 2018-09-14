package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.model.User;

public class SignupCmd {
	
	public static UserManager userManager;

	public SignupCmd(UserManager um) {
		userManager = um;
	}

	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length < 1 || cmds.length > 2) {
			Printer.out("Check help command to see the usage of signup command");
			return;
		}
		User u = cliCtx.getCurrentUser();
		if (u != null) {
			Printer.outln("already signed in as user " + u.getUId() + "\n");
			return;
		}
		run(cmds, cliCtx);
	}
	
	public void run(String[] cmds, CliContext cliCtx) {
		User u = null;
		if (cmds.length == 1) {
			u = userManager.signup();
		}
		if (cmds.length == 2) {
			u = userManager.signup(cmds[1]);
		}
		
		String str = "Signup Success!\n"
				+ "   current ID: " + u.getUId() + " ROLE: " + u.getRole() + "\n"
				+ "   now you can signin with this uId using [signin] command.\n";
		Printer.outln(str);
	}
}
