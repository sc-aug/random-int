package com.debate.vsys.core.manager;

import com.debate.vsys.core.service.MotionService;
import com.debate.vsys.core.service.UserService;
import com.debate.vsys.core.service.VoteService;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.MotionStatus;
import com.debate.vsys.model.Role;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

public class VoteManager {
	
	private VoteService voteService;
	
	private UserService userService;
	
	private MotionService motionService;
	
	public VoteManager(VoteService voteService, UserService userService, MotionService motionService) {
		this.voteService = voteService;
		this.userService = userService;
		this.motionService = motionService;
	}
	
	public Vote vote(String mId, String uId, boolean val) {
		User u = userService.find(uId);
		
		Vote v = new Vote(mId, uId, val);
		
		if (u.getRole() == Role.USER) {			
			userService.addVote(uId, v);
			return voteService.vote(v);
		}
		
		if (u.getRole() == Role.VP) {
			Motion motion = motionService.find(mId);
			if (motion.getStatus() == MotionStatus.TIED) {
				userService.addVote(uId, v);
				return voteService.vote(v);
			}
		}
		
		return null;
	}
	
}
