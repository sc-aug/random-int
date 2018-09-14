package com.demo.vsys;

import static org.junit.Assert.assertTrue;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.demo.vsys.enums.MotionStatus;
import com.demo.vsys.enums.VoteValue;
import com.demo.vsys.exception.InvalidStatusException;

public class MotionTestOnStatus {

	private List<MockVoting> mockVotingList;
	private VP vp;
	private Motion motion;
	private int N;
	private int hesitate;

	@Before
	public void setup() {
		/**
		 * VP
		 * 	VP is not presented
		 */
		vp = new VP(false, VoteValue.YEA);
		/**
		 * Motion: (minimum motion running duration 15 seconds)
		 */
		motion = new Motion(15, ChronoUnit.SECONDS, "a dummy motion description");
		/**
		 * mock voting list
		 */
		N = 81;
		hesitate = 13000; // all votes will submitted in 13 seconds
		Random rand = new Random();
		List<Integer> uIds = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
		Collections.shuffle(uIds);

		mockVotingList = new ArrayList<>();
		for (int i = 0; i < N; i ++) {
			VoteValue val = rand.nextBoolean() ? VoteValue.YEA : VoteValue.NAY;
			MockVoting m = new MockVoting(rand.nextInt(hesitate),
					new Vote(uIds.get(i), motion, val));
			mockVotingList.add(m);
		}
	}

	/**
	 * Stop motion before it starts will get InvalidStatusException
	 */
	@Test(expected = InvalidStatusException.class)
	public void testStopMotionBeforeStart() throws InterruptedException {
		// stop motion
		motion.stop();
	}

	/**
	 * Start motion after it stopped will get InvalidStatusException
	 */
	@Test(expected = InvalidStatusException.class)
	public void testStartMotionAfterStop() throws InterruptedException {
		// start motion
		motion.start();
		// stop motion
		motion.stop();
		// start motion
		motion.start();
	}
	
	/**
	 * If stop early, the method won't take effect, and motion will 
	 * stay in RUNNING status
	 */
	@Test
	public void testStopMotionEarly() throws InterruptedException {
		// start motion
		motion.start();
		Thread.sleep(1000);
		// stop motion
		motion.stop();
		System.out.println(motion.getStatus());
		assertTrue(motion.getStatus() == MotionStatus.RUNNING);
	}

}
