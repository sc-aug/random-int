package com.debate.vsys.cli.cmd;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.Printer;
import com.debate.vsys.core.manager.MotionManager;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.core.manager.VoteManager;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;
import com.debate.vsys.model.VoteStatus;

public class VoteCmd {
	
	private static UserManager userManager;
	private static MotionManager motionManager; 
	private static VoteManager voteManager;
	
	public VoteCmd(UserManager um, MotionManager mm, VoteManager vm) {
		userManager = um;
		motionManager = mm;
		voteManager = vm;
	}
	
	public void parse(String[] cmds, CliContext cliCtx) {
		if (cmds == null || cmds.length != 3) {
			Printer.outln("vote Command usage:\n   vote [motionId] [y/n]");
			return;
		}
		
		User u = cliCtx.getCurrentUser();
		
		if (u == null) return;
		
		u = userManager.find(u.getUId());
		cliCtx.setCurrentUser(u);
		
		run(cmds, cliCtx);
	}
	
	public void run(String[] cmds, CliContext cliCtx) {
		Motion motion = motionManager.find(cmds[1]);
		if (motion == null) return;
		if (! cmds[2].equals("y") && ! cmds[2].equals("n")) return;
		boolean val = cmds[2].equals("y") ? true : false;
		Vote v = voteManager.vote(motion.getMId(), cliCtx.getCurrentUser().getUId(), val);
		
		if (v == null) return;
		
		String str = "Vote submitted:\n   " + v.toString() + "\n";
		
		if (v.getStatus() == VoteStatus.DENIED) {
			str += "But Vote DENIED. REASON: motion not start, motion closed, resubmission\n";
		}
		
		Printer.outln(str);
	}
}
