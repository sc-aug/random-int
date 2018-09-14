package com.debate.vsys.cli.config;

import java.util.Scanner;

import com.debate.vsys.cli.cmd.*;

public class CliResources {
	
	private static Scanner scanner;
	private static HelpCmd help;
	private static SigninCmd signin;
	private static SignupCmd signup;
	private static SignoutCmd signout;
	private static UserCmd user;
	private static MotionCmd motion;
	private static VoteCmd vote;
	
	public CliResources(
			Scanner sc,
			HelpCmd h,
			SigninCmd si,
			SignupCmd su,
			SignoutCmd so,
			UserCmd u,
			MotionCmd m,
			VoteCmd v) {
		scanner = sc;
		help = h;
		signin = si;
		signup = su;
		signout = so;
		user = u;
		motion = m;
		vote = v;
	}
	
	public Scanner getScanner() {
		return scanner;
	}

	public HelpCmd getHelp() {
		return help;
	}

	public SigninCmd getSignin() {
		return signin;
	}

	public SignupCmd getSignup() {
		return signup;
	}

	public SignoutCmd getSignout() {
		return signout;
	}
	
	public UserCmd getUser() {
		return user;
	}

	public MotionCmd getMotion() {
		return motion;
	}

	public VoteCmd getVote() {
		return vote;
	}
	
}
