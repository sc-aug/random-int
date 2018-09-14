package com.demo.vsys;

import com.demo.vsys.enums.VoteValue;

public class VP {
	/**
	 * true  for present
	 * false for absence 
	 */
	private boolean presented;
	
	private VoteValue val;
	
	public VP(boolean p, VoteValue v) {
		this.presented = p;
		this.val = v;
	}
	
	public boolean isPresented() {
		return presented;
	}
	
	public VoteValue getVal() {
		return val;
	}
	
}
