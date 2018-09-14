package com.demo.payal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Motion {

	private static final int MIN_MINUTES = 900;

	public Motion(String topic) {
		super();
		this.topic = topic;
		this.setCurrentState(State.NOT_STARTED);

	}

	private String topic;
	// This map will store the result of voting by counting votes corresponding to
	// favor or against
	private static Map<Boolean, Integer> resultMap = new HashMap<>();
	private Result result;
	private LocalDateTime openVotingTime;
	private LocalDateTime closedVotingTime;
	private List<Future<Vote>> votes = new ArrayList<>();
	private State currentState;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Map<Boolean, Integer> getResultMap() {
		return resultMap;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public LocalDateTime getOpenVotingTime() {
		return openVotingTime;
	}

	public void setOpenVotingTime(LocalDateTime openVotingTime) {
		this.openVotingTime = openVotingTime;
	}

	public LocalDateTime getClosedVotingTime() {
		return closedVotingTime;
	}

	public void setClosedVotingTime(LocalDateTime closedVotingTime) {
		this.closedVotingTime = closedVotingTime;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public List<Future<Vote>> getVotes() {
		return votes;
	}

	public void setVotes(List<Future<Vote>> votes) {
		this.votes = votes;
	}

	public void startVoting() {
		if (this.currentState != State.NOT_STARTED) {
			throw new IllegalStateException("Can only start voting for a new motion.");
		}
		this.setCurrentState(State.OPENED);
		this.setOpenVotingTime(LocalDateTime.now());
		ExecutorService ex = Executors.newCachedThreadPool();
		for (Voter v : Voter.getVoters()) {

			Future<Vote> vote = ex.submit(() -> {
				return gatherVote(v.getVoterId());
			});
			votes.add(vote);
		}
	}

	public void stopVoting() throws InterruptedException, ExecutionException {
		if (this.currentState != State.OPENED) {
			throw new IllegalStateException("Can only stop voting after it was opened.");
		}
		LocalDateTime currentTime = LocalDateTime.now();
		if (Duration.between(currentTime, getOpenVotingTime()).getSeconds() <= MIN_MINUTES) {
			System.out.println("Cannot stop voting before 15 minutes of start");
			return;
		} else {
			setCurrentState(State.CLOSED);
			setClosedVotingTime(currentTime);
			for (Future<Vote> vote : votes) {
				resultMap.computeIfPresent(vote.get().getInFavorOf().isBooleanValue(), (x, y) -> y + 1);
			}
			computeResult();
		}

	}

	private void computeResult() {
		int numberOfVotesInFavor= resultMap.get(true);
		int numberOfVotesAgainst= resultMap.get(false);
		if(numberOfVotesInFavor==numberOfVotesAgainst) {
			currentState= State.TIED;
			computeForTie();
		}else {
			if(numberOfVotesInFavor>numberOfVotesAgainst) {
				result= Result.PASSED;
			}else {
				result= Result.FAILED;
			}
		}
	}

	private void computeForTie() {
		
		gatherVote(Voter.VICE_PRESIDENT.getVoterId());
	}

	private Vote gatherVote(int voterId) {
		System.out.println("Motion to vote for:" + this.getTopic());
		System.out.println("Please enter your vote:");
		System.out.println("Press Y for 'In favor of', N for 'Against' this motion");
		Vote vote = null;
		try (Scanner sc = new Scanner(System.in);) {
			char poll = sc.nextLine().charAt(0);
			vote = new Vote(this, Poll.getPoll(poll), voterId);
		}
		return vote;
	}
}

