package com.demo.vsys;

import static org.junit.Assert.assertTrue;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.demo.vsys.enums.MotionResult;
import com.demo.vsys.enums.VoteValue;

//import static org.junit.Assert.assertNotNull;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.Test;
//
//import com.demo.payal.Motion;

public class MotionTest1 {

	private List<MockVoting> mockVotingList;
	private VP vp;
	private Motion motion;
	
	@Before
	public void setup() {
		/**
		 * VP
		 * 	VP is no presented
		 */
		vp = new VP(false, VoteValue.YEA);
		/**
		 * Motion: (minimum motion running duration 15 seconds)
		 */
		motion = new Motion(15, ChronoUnit.SECONDS, "a dummy motion description");
		/**
		 * mock voting list
		 */
		int N = 81;
		int hesitate = 1300;
		Random rand = new Random();
		List<Integer> uIds = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
		Collections.shuffle(uIds);
		
		mockVotingList = new ArrayList<>();
		for (int i = 0; i < N; i ++) {
			MockVoting m = new MockVoting(rand.nextInt(hesitate),
					new Vote(uIds.get(i), motion, rand.nextBoolean() ? VoteValue.YEA : VoteValue.NAY));
			mockVotingList.add(m);
		}
	}
	
	@Test
	public void testFastStopGetException() throws InterruptedException {
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
		assertTrue(motion.getResult() == MotionResult.PENDING);
		motion.compute(vp);
		assertTrue(motion.getResult() != MotionResult.PENDING);
	}
	
}
