package com.debate.vsys.cli.cmd;

import java.util.List;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public class UserCmd {

	public static UserManager userManager;

	public UserCmd(UserManager um) {
		userManager = um;
	}

	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length != 2) {
			Printer.outln("Check help command to see the usage of user command");
			return;
		}
		run(cmds, cliCtx);
	}

	public void run(String[] cmds, CliContext cliCtx) {
		User u = cliCtx.getCurrentUser();
		if (u == null) return;
		List<Vote> vList = u.getVoteHistory();
		int n = (vList == null) ? 0 : vList.size();
		String str = null;
		if (cmds[1].equals("info")) {
			str = "Current User Info\n"
					+ "   ID: " + u.getUId() + " ROLE: " + u.getRole() + " Voted: " + n + " times\n";

			Printer.outln(str);
		}
		if (cmds[1].equals("voted")) {
			str = "Current User Votes History:\n";
			if (vList != null && !vList.isEmpty()) {
				for (Vote v: vList) {
					str += "   " + v + "\n";
				}
			} else {
				str += "NULL\n";
			}

			Printer.outln(str);
		}

	}
}
