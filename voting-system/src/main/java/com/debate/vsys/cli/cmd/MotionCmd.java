package com.debate.vsys.cli.cmd;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.core.manager.MotionManager;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.Role;
import com.debate.vsys.model.User;

public class MotionCmd {
	
	private static UserManager userManager;
	private static MotionManager motionManager;
	
	public MotionCmd(UserManager um, MotionManager mm) {
		userManager = um;
		motionManager = mm;
	}
	
	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || (cmds.length != 2 && cmds.length != 5)) {
			Printer.outln("Check help command to see the usage of motion command");
			return;
		}
		
		User u = cliCtx.getCurrentUser();
		
		if (u == null) return;
		
		u = userManager.find(u.getUId());
		cliCtx.setCurrentUser(u);
		
		run(cmds, cliCtx);
	}
	
	public void run(String[] cmds, CliContext cliCtx) {
		User u = cliCtx.getCurrentUser();
		
		if (cmds.length == 2) {
			if (cmds[1].equals("list")) {
				if (u.getRole() == Role.VP) return;
				
				String str = "Motions list:\n";
				
				for (Motion m: motionManager.findAll()) {
					str += "   " + m.toString() + "\n";
				}
				
				Printer.outln(str);
			} else if (cmds[1].equals("tied")) {
				String str = "Tied Motions list:\n";
				
				for (Motion m: motionManager.findAllTied()) {
					str += "   " + m.toString() + "\n";
				}
				
				Printer.outln(str);
			} else {
				Motion m = motionManager.find(cmds[1]);
				if (m == null) return;
				String str = "Motion info:\n";
				str += "   " + motionManager.find(cmds[1]).toString() + "\n";
				Printer.outln(str);
			}
		}
		
		if (cmds.length == 5 && cliCtx.getCurrentUser().getRole() == Role.ADMIN) {
			if (! cmds[1].equals("new")) return;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
			LocalDateTime start = LocalDateTime.parse(cmds[3], formatter);
			LocalDateTime end = LocalDateTime.parse(cmds[4], formatter);
			Motion m = motionManager.create(cmds[2], start, end);
			String str = "Motion created!\n"
					+ "   " + m.toString() + "\n";
			Printer.outln(str);
		}
	}// motion new description 2014.08.12.12.12 2015.12.12.10.32
}
