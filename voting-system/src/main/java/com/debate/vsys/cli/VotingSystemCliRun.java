package com.debate.vsys.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.debate.vsys.cli.config.CliResources;

import com.debate.vsys.cli.cmd.HelpCmd;
import com.debate.vsys.cli.cmd.MotionCmd;
import com.debate.vsys.cli.cmd.SigninCmd;
import com.debate.vsys.cli.cmd.SignoutCmd;
import com.debate.vsys.cli.cmd.SignupCmd;
import com.debate.vsys.cli.cmd.UserCmd;
import com.debate.vsys.cli.cmd.VoteCmd;

public class VotingSystemCliRun implements Runnable {
	
	private static Scanner scanner;
	private static HelpCmd help;
	private static SigninCmd signin;
	private static SignupCmd signup;
	private static SignoutCmd signout;
	private static UserCmd user;
	private static MotionCmd motion;
	private static VoteCmd vote;
	
	private static CliContext cliCtx;

	public VotingSystemCliRun(CliResources res, CliContext ctx) {
		scanner = res.getScanner();
		help = res.getHelp();
		signin = res.getSignin();
		signup = res.getSignup();
		signout = res.getSignout();
		user = res.getUser();
		motion = res.getMotion();
		vote = res.getVote();
		cliCtx = ctx;
	}
	
	@Override
	public void run() {
		welcome();
		while (true) {
			inputReader();
		}
	}
	
	private static void inputReader() {
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Printer.out(time + " > ");
		String input = scanner.nextLine();
		parser(input);
	}
	
	private static void parser(String input) {
		commandParser(input.trim().split("\\s+"));
	}
	
	private static void commandParser(String[] commands) {
		
		if (commands == null || commands.length == 0)
			return;
		
		switch(commands[0]) {
		case "": {
			break;
		}
		case "help": {
			help.parse(commands, cliCtx);
			break;
		}
		case "signin": {
			signin.parse(commands, cliCtx);
			break;
		}
		case "signup": {
			signup.parse(commands, cliCtx);
			break;
		}
		case "signout": {
			signout.parse(commands, cliCtx);
			break;
		}
		case "user": {
			user.parse(commands, cliCtx);
			break;
		}
		case "vote": {
			vote.parse(commands, cliCtx);
			break;
		}
		case "motion": {
			motion.parse(commands, cliCtx);
			break;
		}
		default:{
			Printer.outln("invalid input");
		}
		}
	}
	
	private static void welcome() {
		String welcome = 
				  "+---------------------------------------------------+\n"
				+ "|             Welcome to Voting System              |\n"
				+ "+---------------------------------------------------+\n"
				+ ":: use 'help' command to see the availble commands ::\n\n";
		Printer.outln(welcome);
	}
	
}
