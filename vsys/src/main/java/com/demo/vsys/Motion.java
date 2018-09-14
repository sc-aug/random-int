package com.demo.vsys;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.demo.vsys.enums.MotionResult;
import com.demo.vsys.enums.MotionStatus;
import com.demo.vsys.enums.VoteValue;
import com.demo.vsys.exception.InvalidStatusException;

public class Motion {

	private static Object lock = new Object();
	private static Long minDuration;// = 15L;
	private static ChronoUnit timeUnit;// = ChronoUnit.MINUTES;

	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private MotionStatus status;
	private MotionResult result;
	private int yea;
	private int nay;
	// stores userId. prevent resubmission
	private ConcurrentHashMap<Integer, Integer> votedUsers = new ConcurrentHashMap<>();
	private BlockingQueue<Vote> acceptedVotes = new LinkedBlockingQueue<>();

	public Motion(long min, ChronoUnit unit, String description) {
		minDuration = min;
		timeUnit = unit;
		this.description = description;
		this.startTime = null;
		this.endTime = null;
		this.status = MotionStatus.RUNNABLE;
		this.result = MotionResult.PENDING;
	}

	public void submit(Vote vote) {
		if (this.status != MotionStatus.RUNNING) return;
		if (votedUsers.containsKey(vote.getUId()) || acceptedVotes.size() >= 101) return;
		synchronized(lock) {
			if (this.status != MotionStatus.RUNNING) return;
			if (votedUsers.containsKey(vote.getUId()) || acceptedVotes.size() >= 101) return;
			acceptedVotes.offer(vote);
			votedUsers.put(vote.getUId(), 1);
		}
	}

	public void start() {
		if (this.status != MotionStatus.RUNNABLE) {
			throw new InvalidStatusException("Can only start motion of RUNNABLE status. But get status: " + this.status);
		}
		setStartTime(LocalDateTime.now());
		setStatus(MotionStatus.RUNNING);
	}

	public void stop() {
		if (getStatus() != MotionStatus.RUNNING) {
			throw new InvalidStatusException("Can only stop motion of RUNNING status. But get status: " + this.status);
		}
		LocalDateTime timestamp = LocalDateTime.now();
		Duration d1 = Duration.between(startTime, timestamp);
		Duration d2 = Duration.of(minDuration, timeUnit);
		// if motion is not longer than the MIN_DURATION.
		// stop() won't take effect
		if (d1.compareTo(d2) < 0) {
			return;
		} else {
			setEndTime(timestamp);
			setStatus(MotionStatus.STOP);
		}
	}
	
	public void compute(VP vp) {
		for (Vote v: acceptedVotes) {
			if (v.getValue() == VoteValue.YEA) {
				yea ++;
			} else {
				nay ++;
			}
		}
		if (yea == nay) {
			if (vp.isPresented()) {
				boolean vpDecision = vp.getVal() == VoteValue.YEA;
				setResult(vpDecision ? MotionResult.PASSED : MotionResult.FAILED);
			} else {
				setResult(MotionResult.FAILED);
			}
		} else {
			setResult(yea > nay ? MotionResult.PASSED : MotionResult.FAILED);
		}
		setStatus(MotionStatus.TERMINATED);
	}
	

	/**
	 * 
	 * Getters Setters
	 * 
	 */
	public static Long getMinDuration() {
		return minDuration;
	}

	public static void setMinDuration(Long minDuration) {
		Motion.minDuration = minDuration;
	}

	public static ChronoUnit getTimeUnit() {
		return timeUnit;
	}

	public static void setTimeUnit(ChronoUnit timeUnit) {
		Motion.timeUnit = timeUnit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public MotionStatus getStatus() {
		return status;
	}

	public void setStatus(MotionStatus status) {
		this.status = status;
	}

	public MotionResult getResult() {
		return result;
	}

	public void setResult(MotionResult result) {
		this.result = result;
	}

	public int getYea() {
		return yea;
	}

	public void setYea(int yea) {
		this.yea = yea;
	}

	public int getNay() {
		return nay;
	}

	public void setNay(int nay) {
		this.nay = nay;
	}

}
