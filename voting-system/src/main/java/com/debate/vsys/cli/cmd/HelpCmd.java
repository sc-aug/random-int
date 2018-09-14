package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.model.User;

public class HelpCmd {
	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length != 1) {
			Printer.outln("Check usage of help command");
		}
		run(cliCtx);
	}
	
	public void run(CliContext cliCtx) {
		User u = cliCtx.getCurrentUser();
		String format = "%-40s%-20s%s";
		String str = "Commands:\n";
		str += Cmd.str(format, "   * help", "[user,vp,admin]", "show available commands\n");
		
		if (u == null) {
			str = str
					+ Cmd.str(format, "   * signin [userId]", "[user,vp,admin]", "login to use the voting system\n")
					+ Cmd.str(format, "   * signup", "[user,vp,admin]", "signup as new user\n")
					+ Cmd.str(format, "   * signup [secret]", "[user,vp,admin]", "signup as new admin or vp\n");
		} else {
			str = str
					+ Cmd.str(format, "   * signout", "[user,vp,admin]", "exit current session\n")
					+ Cmd.str(format, "   * motion list", "[user,admin]", "show all the motions\n")
					+ Cmd.str(format, "   * motion tied", "[vp]", "show all the tied motions\n")
					+ Cmd.str(format, "   * motion new [name] [start] [end]", "[admin]", "add a new motion. time format [yyyy.MM.dd.HH.mm]\n")
					+ Cmd.str(format, "   * vote [motionId] [y/n]", "[user]", "vote for a motion\n")
					+ Cmd.str(format, "   * user info", "[user,vp,admin]", "show current user info\n")
					+ Cmd.str(format, "   * user voted", "[user,vp]", "show current user votes\n");
		}
		
		Printer.outln(str);
	}
}
