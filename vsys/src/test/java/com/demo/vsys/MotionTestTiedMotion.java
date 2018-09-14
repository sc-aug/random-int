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

public class MotionTestTiedMotion {

	private List<MockVoting> mockVotingList;
	private VP vp;
	private Motion motion;
	private int N;
	private int hesitate;
	
	@Before
	public void setup() {
		/**
		 * Motion: (minimum motion running duration 15 seconds)
		 */
		motion = new Motion(15, ChronoUnit.SECONDS, "a dummy motion description");
		/**
		 * mock voting list
		 */
		N = 80;
		hesitate = 13000; // all votes will submitted in 13 seconds
		Random rand = new Random();
		List<Integer> uIds = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
		Collections.shuffle(uIds);
		
		mockVotingList = new ArrayList<>();
		for (int i = 0; i < N; i ++) {
			VoteValue val = i < 40 ? VoteValue.YEA : VoteValue.NAY;
			MockVoting m = new MockVoting(rand.nextInt(hesitate),
					new Vote(uIds.get(i), motion, val));
			mockVotingList.add(m);
		}
	}
	
	/**
	 * 40 Yea 40 Nay, and VP is not present
	 */
	@Test
	public void testTiedMotionWithoutVP() throws InterruptedException {
		/**
		 * VP
		 * 	 VP is not presented
		 */
		vp = new VP(false, VoteValue.YEA);
		
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
		// number of votes should be N
		assertEquals(N, motion.getYea() + motion.getNay());
		// VP is not present. TIED motion will result as FAILED
		assertTrue(motion.getResult() == MotionResult.FAILED);
	}
	
	/**
	 * 40 Yea 40 Nay, and VP is present with Yea
	 */
	@Test
	public void testTiedMotionWithVP() throws InterruptedException {
		/**
		 * VP
		 * 	 VP is not presented
		 */
		vp = new VP(true, VoteValue.YEA);
		
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
		// Yea equals to Nay
		assertEquals(motion.getYea(), motion.getNay());
		// VP is not present. TIED motion will result as FAILED
		assertTrue(motion.getResult() == MotionResult.PASSED);
	}
	
}
