package com.demo.vsys;

import static org.junit.Assert.assertEquals;
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

import com.demo.vsys.enums.MotionResult;
import com.demo.vsys.enums.VoteValue;

public class MotionTestOnVotingOver101 {

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
		N = 1000;
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
	 * Limit on number of votes is 101, now 1000 votes submitted
	 * during motion RUNNING status.
	 * Only the first 101 votes will be count, the rest 899 votes
	 * will be rejected. 
	 */
	@Test
	public void testOnVotingOver101() throws InterruptedException {
		// start motion
		motion.start();
		// perform voting
		for (MockVoting m: mockVotingList) {
			(new Thread(m)).start();
		}
		// sleep
		Thread.sleep(Motion.getMinDuration() * 1000);
		// stop motion
		motion.stop();
		// before compute the result. result is PENDING
		assertTrue(motion.getResult() == MotionResult.PENDING);
		motion.compute(vp);
		// after compute the result will be PASSED or FAILED
		assertTrue(motion.getResult() != MotionResult.PENDING);
		// only the very first vote will count
		assertEquals(101, motion.getYea() + motion.getNay());
	}
	
}
