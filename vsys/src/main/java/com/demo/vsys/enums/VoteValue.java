package com.demo.vsys.enums;

public enum VoteValue {

	YEA(true),
	NAY(false);
	
	private boolean val;
	
	private VoteValue(boolean v) {
		val = v;
	}

}
