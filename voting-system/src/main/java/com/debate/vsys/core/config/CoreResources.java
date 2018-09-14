package com.debate.vsys.core.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.debate.vsys.model.Motion;
import com.debate.vsys.model.UMId;
import com.debate.vsys.model.User;
import com.debate.vsys.model.Vote;

/**
 * 
 * One place for data storage
 * 
 */
public class CoreResources {
	
	/**
	 * Store all the motion data
	 */
	private ConcurrentHashMap<String, Motion> motionsDb;
	
	/**
	 * This queue racing the preactive/active motions.
	 * 
	 * The RUNNABLE/RUNNING/PENDING motion will be keeped
	 * The TIED/TERMINATED motion will be removed from the queue
	 */
	private BlockingQueue<Motion> activeMotionsQueue;
	
	/**
	 * This queue tracing the preactive/active motions.
	 * 
	 * The TIED motion will be keeped and waiting for VP to vote
	 */
	private BlockingQueue<Motion> tiedMotionsQueue;
	
	/**
	 * Motions in this queue only status of BEFORETERM
	 * And motions in queue are ready to give vote result
	 * 
	 * These motions comes from situations:
	 *   - votes reaches 101
	 *   - after VP vote
	 *   - VP didn't vote and motion timeout
	 */
	private BlockingQueue<Motion> termMotionsQueue;
	
	/**
	 * Store all the user data
	 */
	private ConcurrentHashMap<String, User> usersDb;
	
	/**
	 * Queuing all the votes, queue as a buffer
	 */
	private BlockingQueue<Vote> votesQueue;
	
	/**
	 * Store info of passed votes. For user to check vote status and result.
	 * Mapping entry if type of <String, Vote>
	 */
	private ConcurrentHashMap<String, Vote> votesDb;
	
	/**
	 * For vote validation.
	 */
	private ConcurrentHashMap<UMId, String> umIdStorage;
	
	public CoreResources(
			ConcurrentHashMap<String, Motion> motionsDb,
			BlockingQueue<Motion> activeMotionsQueue,
			BlockingQueue<Motion> tiedMotionsQueue,
			BlockingQueue<Motion> termMotionsQueue,
			ConcurrentHashMap<String, User> usersDb,
			BlockingQueue<Vote> votesQueue,
			ConcurrentHashMap<String, Vote> votesDb,
			ConcurrentHashMap<UMId, String> umIdStorage
			) {
		this.motionsDb = motionsDb;
		this.activeMotionsQueue = activeMotionsQueue;
		this.tiedMotionsQueue = tiedMotionsQueue; 
		this.termMotionsQueue = termMotionsQueue;
		this.usersDb = usersDb;
		this.votesQueue = votesQueue;
		this.votesDb = votesDb;
		this.umIdStorage = umIdStorage;
	}

	public ConcurrentHashMap<String, Motion> getMotionsDb() {
		return motionsDb;
	}
	
	public BlockingQueue<Motion> getActiveMotionsQueue() {
		return activeMotionsQueue;
	}
	
	public BlockingQueue<Motion> getTiedMotionsQueue() {
		return tiedMotionsQueue;
	}
	
	public BlockingQueue<Motion> getTermMotionsQueue() {
		return termMotionsQueue;
	}

	public ConcurrentHashMap<String, User> getUsersDb() {
		return usersDb;
	}

	public BlockingQueue<Vote> getVotesQueue() {
		return votesQueue;
	}

	public ConcurrentHashMap<String, Vote> getVotesDb() {
		return votesDb;
	}

	public ConcurrentHashMap<UMId, String> getUmIdStorage() {
		return umIdStorage;
	}
	
}
