package com.debate.vsys.core.config;

/**
 * System properties
 */
public class CoreProperties {
	/**
	 * grace time for vote workers to process RUNNABLE votes
	 */
	private static long timeoutVWSeconds;
	
	/**
	 * grace time for VP to process TIED votes
	 */
	private static long timeoutVPSeconds;
	
	/**
	 * Timeout for BlockingQueue
	 */
	private static long timeoutDequeueVotesQueue;
	private static long timeoutDequeueActiveMotionsQueue;
	private static long timeoutDequeueTiedMotionsQueue;
	private static long timeoutDequeueTermMotionsQueue;
	
	
	public CoreProperties(
			long toVW,
			long toVP,
			long toVote,
			long toActive,
			long toTied,
			long toTerm) {
		timeoutVWSeconds = toVW;
		timeoutVPSeconds = toVW + toVP;
		timeoutDequeueVotesQueue = toVote;
		timeoutDequeueActiveMotionsQueue = toActive;
		timeoutDequeueTiedMotionsQueue = toTied;
		timeoutDequeueTermMotionsQueue = toTerm;
	}
	
	public long getTimeoutVWSec() {
		return timeoutVWSeconds;
	}
	
	public long getTimeoutVPSec() {
		return timeoutVPSeconds;
	}

	public long getTimeoutDequeueVotesQueue() {
		return timeoutDequeueVotesQueue;
	}

	public long getTimeoutDequeueActiveMotionsQueue() {
		return timeoutDequeueActiveMotionsQueue;
	}

	public long getTimeoutDequeueTiedMotionsQueue() {
		return timeoutDequeueTiedMotionsQueue;
	}
	
	public long getTimeoutDequeueTermMotionsQueue() {
		return timeoutDequeueTermMotionsQueue;
	}
	
}
