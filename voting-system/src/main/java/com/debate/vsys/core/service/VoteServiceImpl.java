package com.debate.vsys.core.service;

import com.debate.vsys.core.data.VoteDao;
import com.debate.vsys.model.Vote;

public class VoteServiceImpl implements VoteService {

	private VoteDao voteDao; 
	
	public VoteServiceImpl(VoteDao voteDao) {
		this.voteDao = voteDao;
	}
	
	@Override
	public Vote vote(Vote v) {
		voteDao.enqueue(v);
		return v;
	}

}
