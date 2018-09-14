package com.debate.vsys.core.data;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.debate.vsys.core.config.CoreProperties;
import com.debate.vsys.core.config.CoreResources;
import com.debate.vsys.core.exception.InvalidMotionStatus;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.MotionResult;
import com.debate.vsys.model.MotionStatus;
import com.debate.vsys.model.Vote;
import com.debate.vsys.model.VoteStatus;

/**
 * The VoteWorker and ActiveMotionWorker are the core part of the system.
 * 
 * 	  VoteWorker:
 *      - Consume votes, validate votes, count valid votes for motion
 *    ActiveMotionWorker:
 *      - Tracing motion status and change motion status.
 *      - Push motion to TiedMotionQueue
 *    TiedMotionWorker
 *    	- Keep the TIED motions
 *      - Remove timeout TIED motions
 *    TermMotionWorker
 *      - Give result to motions
 */
public class CoreWorkers implements Runnable{

	/**
	 * All the votes in queue, will be consumed by VoteWorker
	 */
	private static BlockingQueue<Vote> votesQueue;

	/**
	 * This queue tracing the preactive/active motions.
	 * 
	 * The RUNNABLE/RUNNING/PENDING motion will be keeped
	 * The TIED/TERMINATED motion will be removed from the queue
	 */
	private static BlockingQueue<Motion> activeMotionsQueue;

	/**
	 * This queue tracing the tied motions for VP to process
	 * Status of TIED motions only.
	 * If VP voting timeout. Change status from TIED to TERMINATE
	 */
	private static BlockingQueue<Motion> tiedMotionsQueue;

	/**
	 * Motions in this queue only status of BEFORETERM
	 * And motions in queue are ready to give vote result
	 * 
	 * These motions comes from situations:
	 *   - votes reaches 101
	 *   - after VP vote
	 *   - VP didn't vote and motion timeout
	 */
	private static BlockingQueue<Motion> termMotionsQueue;

	/**
	 * Keep motion data
	 */
	private static ConcurrentHashMap<String, Motion> motionsDb;

	/**
	 * Grace time for vote workers to process RUNNABLE votes
	 * This property used in MotionWorker, for updating motion status.
	 * TimeUnit: Second
	 * 
	 * VoteWorker can still process votes for PENDING motion.
	 * PENDING time: from `endTime` to `endTime + timeout`
	 */
	private static long timeoutVWSeconds;

	/**
	 * Grace time for VP to process TIED votes
	 * TimeUnit: Second
	 */
	private static long timeoutVPSeconds;

	/**
	 * Timeout for different BlockingQueue
	 * TimeUnit: Second
	 */
	private static long timeoutDequeueVotesQueue;
	private static long timeoutDequeueActiveMotionsQueue;
	private static long timeoutDequeueTiedMotionsQueue;
	private static long timeoutDequeueTermMotionsQueue;

	/**
	 * start new threads responsible 
	 * for processing votes and processing motion
	 */
	@Override
	public void run() {
		Thread voteWorker = new Thread(new VoteWorker());
		Thread activeMWorker = new Thread(new ActiveMotionWorker());
		Thread tiedMWorker = new Thread(new TiedMotionWorker());
		Thread termMWorker = new Thread(new TermMotionWorker());

		voteWorker.start();
		activeMWorker.start();
		tiedMWorker.start();
		termMWorker.start();
	}

	/**
	 * Constructor
	 * 	 load required resources and properties
	 */
	public CoreWorkers(CoreResources res, CoreProperties prop) {
		// DB
		motionsDb = res.getMotionsDb();
		// Queue
		votesQueue = res.getVotesQueue();
		activeMotionsQueue = res.getActiveMotionsQueue();
		tiedMotionsQueue = res.getTiedMotionsQueue();
		termMotionsQueue = res.getTermMotionsQueue();
		// Timeout
		timeoutVWSeconds = prop.getTimeoutVWSec();
		timeoutVPSeconds = prop.getTimeoutVPSec();
		timeoutDequeueVotesQueue = prop.getTimeoutDequeueVotesQueue();
		timeoutDequeueActiveMotionsQueue = prop.getTimeoutDequeueActiveMotionsQueue();
		timeoutDequeueTiedMotionsQueue = prop.getTimeoutDequeueTiedMotionsQueue();
		timeoutDequeueTermMotionsQueue = prop.getTimeoutDequeueTermMotionsQueue();
	}

	/**
	 * Dequeue function for any BlockingQueue.
	 * Used in VoteWorker, MotionWorker and TiedMotionWorker
	 * @param q
	 * @param seconds
	 * @return null or T object
	 */
	private static <T> T deQueue(BlockingQueue<T> q, long seconds) {
		T ret = null;
		try {
			ret = q.poll(seconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Vote worker
	 */
	private static class VoteWorker implements Runnable {
		@Override
		public void run() {
			// waiting for q to be initialized
			while (votesQueue == null);
			// keep on reading votes from queue
			while (true) {
				Vote v = deQueue(votesQueue, timeoutDequeueVotesQueue);
				if (v != null) {
					processVote(v);
				}
			}
		}

		/**
		 * vote processor read vote and check vote & motion validation
		 */
		private static void processVote(Vote v) {
			if (v == null) return;
			Motion m = motionsDb.get(v.getMId());
			boolean vPass = false;

			// if motion status is RUNNABLE or RUNNING or PENDING
			if (m.getStatus().ordinal() <= MotionStatus.PENDING.ordinal()) {
				LocalDateTime endTime = m.getEndTime();
				LocalDateTime startTime = m.getStartTime();
				// if vote happens during motion active time
				if (endTime.isAfter(v.getTime()) && startTime.isBefore(v.getTime())) {

					// if motion reaches 101 votes
					if (m.getYeas() + m.getNays() < 101) {
						synchronized(m) {
							if (m.getYeas() + m.getNays() < 101) {
								if (v.isVal()) {
									m.setYeas(m.getYeas() + 1);
								} else {
									m.setNays(m.getNays() + 1);
								}
								vPass = true;
							}
						}
					}
				}
			}

			v.setStatus(vPass ? VoteStatus.PASSED : VoteStatus.DENIED);
		}
	}

	/**
	 * ActiveMotion worker 
	 */
	private static class ActiveMotionWorker implements Runnable {

		@Override
		public void run() {
			// waiting for q to be initialized
			while (activeMotionsQueue == null);
			// keep on processing motions
			while (true) {
				Motion m = deQueue(activeMotionsQueue, timeoutDequeueActiveMotionsQueue);
				if (m != null) {
					if (processActiveMotion(m)) { // true: remain in active queue
						activeMotionsQueue.offer(m);
					} else {
						switch(m.getStatus()) { // false: leave queue, enter next queue
						case TIED: {
							tiedMotionsQueue.offer(m);
							break;
						}
						case BEFORETERM: {
							termMotionsQueue.offer(m);
							break;
						}
						default: {
							throw new InvalidMotionStatus("ActiveMotion.run() get motion of status: " + m.getStatus());
						}
						}
					}
				}
			}
		}

		/**
		 * Monitor motions and Change motion status
		 *   RUNNABLE:		time < startTime
		 *   RUNNING: 		time > startTime && time < endTime
		 *   PENDING:		time > endTime && time < endTime + graceTime min
		 *   				(graceTime is grace time for voteworker processing votes)
		 *   TIED:			time > endTime + graceTime min && yeas == nays
		 *   BEFORETERM: 	time > endTime + graceTime min && yeas != nays
		 *   
		 * Return boolean value
		 * 	 True:			Motion is in status of RUNNABLE/RUNNING/PENDING
		 * 	 False:			Motion is in status of TIED/TERMINATED 
		 */
		private static boolean processActiveMotion(Motion m) {
			if (m == null) return false;

			LocalDateTime start = m.getStartTime();
			LocalDateTime end = m.getEndTime();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime timeout = end.plusSeconds(timeoutVWSeconds);

			synchronized(m) {

				switch(m.getStatus()) {
				case RUNNABLE: {

					if (now.isAfter(timeout)) {
						// check tied or not
						if (m.getYeas() == m.getNays()) {
							m.setStatus(MotionStatus.TIED);
							return false;
						} else {
							m.setStatus(MotionStatus.BEFORETERM);
							return false;
						}
					}
					if (now.isAfter(end)) {
						m.setStatus(MotionStatus.PENDING);
					} else if (now.isAfter(start)) {
						m.setStatus(MotionStatus.RUNNING);
					} else {
						// stay in RUNNABLE status
					}
					break;
				}
				case RUNNING: {

					if (now.isAfter(timeout)) {
						// check tied or not
						if (m.getYeas() == m.getNays()) {
							m.setStatus(MotionStatus.TIED);
							return false;
						} else {
							m.setStatus(MotionStatus.BEFORETERM);
							return false;
						}
					} else if (now.isAfter(end)) {
						m.setStatus(MotionStatus.PENDING);
					} else { // still in running status
						if (m.getYeas() + m.getNays() >= 101) {
							m.setStatus(MotionStatus.TIED);
							return false;
						}
						// stay in RUNNING status
					}
					break;
				}
				case PENDING: {
					if (now.isAfter(timeout)) {
						// check tied or not
						if (m.getYeas() == m.getNays()) {
							m.setStatus(MotionStatus.TIED);
							return false;
						}
						m.setStatus(MotionStatus.BEFORETERM);
						return false;
					}
					break;
				}
				default: { // no default. only can get null; 
					throw new InvalidMotionStatus("processActiveMotion get motion of status: " + m.getStatus());
				}
				}

			}

			return true;
		}
	}

	/**
	 * TiedMotion worker 
	 */
	private static class TiedMotionWorker implements Runnable {

		@Override
		public void run() {
			// waiting for q to be initialized
			while (tiedMotionsQueue == null);
			// keep on processing motions
			while (true) {
				Motion m = deQueue(tiedMotionsQueue, timeoutDequeueTiedMotionsQueue);
				// A TIED motion can be voted by VP.
				// After VP vote, motion status will change to TERMINATED
				if (m != null) {
					if (processTiedMotion(m)) { // remain TIED
						tiedMotionsQueue.offer(m);
					} else { // timeout motions enter BEFORETERM
						termMotionsQueue.offer(m);
					}
				}
			}
		}

		/**
		 * Mointoring on TiedMotionQueue.
		 * If VP not vote before timeout, motion will change status to BEFORETERM. 
		 * @return
		 */
		private static boolean processTiedMotion(Motion m) {
			if (m == null) return false;

			LocalDateTime end = m.getEndTime();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime timeout = end.plusSeconds(timeoutVPSeconds);

			synchronized(m) {
				if (m.getStatus() == MotionStatus.TIED) {
					if (now.isBefore(timeout)) {
						return true;
					} else {
						m.setStatus(MotionStatus.BEFORETERM);
						return false;
					}
				} else {
					throw new InvalidMotionStatus("processTiedMotion get motion of status: " + m.getStatus());
				}
			}
		}

	}

	/**
	 * TermMotion worker
	 * Process the motion from BEFREOTERM to TERMINATE
	 */
	private static class TermMotionWorker implements Runnable {

		@Override
		public void run() {
			// waiting for q to be initialized
			while (termMotionsQueue == null);
			// keep on processing motions
			while (true) {
				Motion m = deQueue(termMotionsQueue, timeoutDequeueTermMotionsQueue);
				if (m != null) {
					processTermMotion(m);
				}
			}
		}
		
		private static void processTermMotion(Motion m) {
			if (m == null) return;
			synchronized(m) {
				m.setStatus(MotionStatus.TERMINATED);
				if (m.getYeas() > m.getNays()) {
					m.setResult(MotionResult.PASSED);
				} else {
					m.setResult(MotionResult.FAILED);
				}
			}
		}

	}
}
