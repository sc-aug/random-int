package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;

public class SignoutCmd {
	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length != 1) {
			Printer.outln("Check help command to see the usage of signout command");
			return;
		}
		
		run(cmds, cliCtx);
	}
	
	public void run(String[] cmds, CliContext cliCtx) {
		cliCtx.setCurrentUser(null);
		String str = "Signout Success!\n"
				+ "   use help command see what you can do\n";
		Printer.outln(str);
	}
}
