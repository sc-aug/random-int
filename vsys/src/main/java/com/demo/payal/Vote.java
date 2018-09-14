package com.demo.payal;

public class Vote {
	
	private Motion motion;
	private Poll inFavorOf;
	private int voterId;
	public Vote(Motion motion, Poll poll, int voterId) {
		this.motion= motion;
		this.inFavorOf= poll;
		this.voterId= voterId;
	}
	public Motion getMotion() {
		return motion;
	}
	public void setMotion(Motion motion) {
		this.motion = motion;
	}
	public Poll getInFavorOf() {
		return inFavorOf;
	}
	public void setInFavorOf(Poll inFavorOf) {
		this.inFavorOf = inFavorOf;
	}
	public int getVoterId() {
		return voterId;
	}
	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}

}
