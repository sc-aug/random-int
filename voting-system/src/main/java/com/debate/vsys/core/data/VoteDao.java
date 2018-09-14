package com.debate.vsys.core.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.debate.vsys.core.config.CoreResources;
import com.debate.vsys.model.UMId;
import com.debate.vsys.model.Vote;
import com.debate.vsys.model.VoteStatus;

public class VoteDao {

	/**
	 * Queuing all the votes, queue as a buffer
	 */
	private static BlockingQueue<Vote> votesQueue;

	/**
	 * Store info of passed votes. For user to check vote status and result.
	 * Mapping entry if type of <String, Vote>
	 */
	private static ConcurrentHashMap<String, Vote> votesDb;

	/**
	 * For vote validation
	 */
	private static ConcurrentHashMap<UMId, String> umIdStorage; 

	/**
	 * Lock of umIdStorage
	 */
	private static Object umIdStorageLock;

	public VoteDao(CoreResources res) {
		VoteDao.votesQueue = res.getVotesQueue();
		VoteDao.votesDb = res.getVotesDb();
		VoteDao.umIdStorage = res.getUmIdStorage();
		umIdStorageLock = new Object();
	}

	public boolean enqueue(Vote vote) {
		boolean res = false;

		votesDb.put(vote.getVId(), vote);

		if (! umIdStorage.containsKey(vote.getUMId())) {
			synchronized(umIdStorageLock) {
				if (! umIdStorage.containsKey(vote.getUMId())) {
					umIdStorage.put(vote.getUMId(), vote.getTime().toString());
					votesQueue.offer(vote);
				}
				res = true;
			}
		}

		// invalid vote won't be put in vote queue.
		// and mark Status as DENIED.
		// denied reason: 1 user 1 motion, only allow 1 vote
		if (! res) {
			vote.setStatus(VoteStatus.DENIED);
		}

		return res;
	}

}
