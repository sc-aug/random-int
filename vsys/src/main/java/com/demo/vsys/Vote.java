package com.demo.vsys;

import com.demo.vsys.enums.VoteValue;

public class Vote {

	private int uId;
	private Motion motion;
	private VoteValue value;

	public Vote(int uId, Motion m, VoteValue val) {
		this.uId = uId;
		this.motion = m;
		this.value = val;
	}

	public int getUId() {
		return uId;
	}

	public void setUId(int uId) {
		this.uId = uId;
	}

	public Motion getMotion() {
		return motion;
	}

	public void setMotion(Motion motion) {
		this.motion = motion;
	}

	public VoteValue getValue() {
		return value;
	}

	public void setValue(VoteValue value) {
		this.value = value;
	}

}
