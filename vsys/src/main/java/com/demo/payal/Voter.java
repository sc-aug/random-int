package com.demo.payal;

import java.util.Set;
import java.util.TreeSet;

public class Voter implements Comparable<Voter>{
	//President having voter id as 0
	public static final Voter VICE_PRESIDENT= new Voter(0);
	private static Set<Voter> voters= new TreeSet<>();
	//Adding 101 voters at begining of program
	static {
		// 	voters.add(VICE_PRESIDENT);
			 for(int i=1; i<=101; i++) {
				 voters.add(new Voter(i));
			 }
		 }

	private int voterId;
	public Voter(int voterId) {
		super();
		this.voterId = voterId;
	}
	public int getVoterId() {
		return voterId;
	}
	public static Set<Voter> getVoters() {
		return voters;
	}
	public static void addVoter(Voter voter) {
		voters.add(voter);
	}
	@Override
	public int compareTo(Voter voter) {
		return this.getVoterId()-voter.getVoterId();
	}

}
