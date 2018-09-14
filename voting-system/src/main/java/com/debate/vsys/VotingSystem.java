package com.debate.vsys;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.debate.vsys.cli.CliContext;
import com.debate.vsys.cli.VotingSystemCliRun;
import com.debate.vsys.cli.cmd.HelpCmd;
import com.debate.vsys.cli.cmd.MotionCmd;
import com.debate.vsys.cli.cmd.SigninCmd;
import com.debate.vsys.cli.cmd.SignoutCmd;
import com.debate.vsys.cli.cmd.SignupCmd;
import com.debate.vsys.cli.cmd.UserCmd;
import com.debate.vsys.cli.cmd.VoteCmd;
import com.debate.vsys.cli.config.CliResources;
import com.debate.vsys.core.config.CoreProperties;
import com.debate.vsys.core.config.CoreResources;
import com.debate.vsys.core.data.CoreWorkers;
import com.debate.vsys.core.data.MotionDao;
import com.debate.vsys.core.data.UserDao;
import com.debate.vsys.core.data.VoteDao;
import com.debate.vsys.core.manager.MotionManager;
import com.debate.vsys.core.manager.UserManager;
import com.debate.vsys.core.manager.VoteManager;
import com.debate.vsys.core.service.MotionService;
import com.debate.vsys.core.service.MotionServiceImpl;
import com.debate.vsys.core.service.UserService;
import com.debate.vsys.core.service.UserServiceImpl;
import com.debate.vsys.core.service.VoteService;
import com.debate.vsys.core.service.VoteServiceImpl;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.UMId;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public class VotingSystem {

	public static void main(String[] args) {
		
		CoreResources cr = initCoreResources(); 
		CoreProperties cp = initCoreProperties();
		CliResources cliRes = initCliResources(cr, cp);
		CliContext cliCtx = new CliContext();
		
		CoreWorkers cworkers = new CoreWorkers(cr, cp);
		(new Thread(cworkers)).run();
		
		VotingSystemCliRun cliRun = new VotingSystemCliRun(cliRes, cliCtx);		
		(new Thread(cliRun)).run();
		
	}
	
	private static CoreResources initCoreResources() {
		ConcurrentHashMap<String, Motion> motionsDb = new ConcurrentHashMap<>();
		BlockingQueue<Motion> activeMotionsQueue = new LinkedBlockingQueue<>();
		BlockingQueue<Motion> tiedMotionsQueue = new LinkedBlockingQueue<>();
		BlockingQueue<Motion> termMotionsQueue = new LinkedBlockingQueue<>();
		ConcurrentHashMap<String, User> usersDb = new ConcurrentHashMap<>();
		BlockingQueue<Vote> votesQueue = new LinkedBlockingQueue<>();
		ConcurrentHashMap<String, Vote> votesDb = new ConcurrentHashMap<>();
		ConcurrentHashMap<UMId, String> umIdStorage = new ConcurrentHashMap<>();
		return new CoreResources(motionsDb, activeMotionsQueue, tiedMotionsQueue, termMotionsQueue, usersDb, votesQueue, votesDb, umIdStorage);
	}
	
	private static CliResources initCliResources(CoreResources cr, CoreProperties cp) {
		UserDao ud = new UserDao(cr);
		UserService us = new UserServiceImpl(ud);
		UserManager um = new UserManager(us);
		
		MotionDao md = new MotionDao(cr);
		MotionService ms = new MotionServiceImpl(md);
		MotionManager mm = new MotionManager(ms);
		
		VoteDao vd = new VoteDao(cr);
		VoteService vs = new VoteServiceImpl(vd);
		VoteManager vm = new VoteManager(vs, us, ms);
		
		Scanner sc = new Scanner(System.in);
		HelpCmd help = new HelpCmd();
		SigninCmd signin = new SigninCmd(um);
		SignupCmd signup = new SignupCmd(um);
		SignoutCmd signout = new SignoutCmd();
		UserCmd user = new UserCmd(um);
		MotionCmd motion = new MotionCmd(um, mm);
		VoteCmd vote = new VoteCmd(um, mm, vm);
		
		return new CliResources(sc, help, signin, signup, signout, user, motion, vote);
	}
	
	private static CoreProperties initCoreProperties() {
		return new CoreProperties(
						60 * 10, // long toVW,
						60 * 10, // long toVP,
						10, // long toVote,
						10, // long toActive,
						10, // long toTied,
						10  //long toTerm
						);
	}

}
