package com.demo.payal;

public enum Poll {

	Y('Y', true), N('N', false);
	
	char charValue;
	boolean booleanValue;
	Poll(char c, boolean b){
		this.charValue= c;
		this.booleanValue= b;
	}
	
	public static Poll getPoll(char c){
		if(c=='Y'|| c=='y') {
			return Poll.Y;
		}else {
			return Poll.N;
		}
	}

	public char getCharValue() {
		return charValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}
	
}
