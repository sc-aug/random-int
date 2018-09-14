package com.demo.vsys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.demo.payal.Motion;
import com.demo.payal.State;

public class MotionTest {

	@Test
	public void testForSuccess() throws InterruptedException, ExecutionException {
		Motion motion= new Motion("Should employees be allowed to work on public holidays?");
		motion.startVoting();
		Thread.sleep(200000);
		motion.stopVoting();
		assertNotNull(motion.getResult());
	}

	@Test
	public void testForVotingLessThan15Mins() throws InterruptedException, ExecutionException {
		Motion motion= new Motion("Should employees be allowed to work on public holidays?");
		motion.startVoting();
		Thread.sleep(20000);
		motion.stopVoting();
		assertEquals(State.OPENED,motion.getCurrentState());
	}
}
