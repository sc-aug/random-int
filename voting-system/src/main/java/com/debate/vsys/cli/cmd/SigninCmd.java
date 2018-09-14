package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.model.User;

public class SigninCmd {

	public static UserManager userManager;

	public SigninCmd(UserManager um) {
		userManager = um;
	}

	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length != 2) {
			Printer.outln("Check help command to see the usage of signin command");
			return;
		}

		if (cmds[1].equals("list")) {
			
		}
		
		String uId = cmds[1];
		User u = userManager.find(uId);

		if (u != null) {
			run(cmds, cliCtx, u);
		}

	}

	public void run(String[] cmds, CliContext cliCtx, User u) {
		cliCtx.setCurrentUser(u);
		String str = "Signin Success!\n"
				+ "   current ID: " + u.getUId() + " ROLE: " + u.getRole() + "\n";
		Printer.outln(str);
	}

}
