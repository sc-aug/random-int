package com.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.service.PartService;
import com.demo.service.PartServiceImpl;

public class RemovePartsTest {

	private static PartService service;

	private static int N;

	private static List<Integer> randomIds;

	/**
	 * Propergate DB with data
	 */
	@Before
	public void setup() {
		service = new PartServiceImpl();
		N = 100;
		CountDownLatch countDownLatch = new CountDownLatch(N);
		ExecutorService ex = Executors.newCachedThreadPool();

		for (int i = 0; i < N; i++) {
			ex.execute(() -> {
				try {
					service.addPart(new Part("Foo", "Bar", 2000));
				} catch (InvalidPartException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			});
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// random ids for remove test
		randomIds = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
		Collections.shuffle(randomIds);
	}

	/**
	 * Remove 30 random car parts
	 */
	@Test
	public void testCommonRemove() {

		int deleteNum = 30;
		for (int i = 0; i < deleteNum; i ++) {
			service.removePartById(randomIds.get(i));
		}

		assertEquals(N - deleteNum, service.getNumberOfParts());
	}

	/**
	 * Remove 70 random parts
	 */
	@Test
	public void testMultithreadRemove() {
		int deleteNum = 50;
		CountDownLatch latch = new CountDownLatch(deleteNum);
		for (int i = deleteNum; i < N; i ++) {
			new Thread(new Task(latch, randomIds.get(i))).start();
		}
		
		try {
			latch.await(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(service.getNumberOfParts(), 50);
	}

	private static class Task implements Runnable {

		private CountDownLatch latch;
		private final long id;

		public Task(CountDownLatch latch, long id) {
			this.id = id;
			this.latch = latch;
		}

		@Override
		public void run() {
			service.removePartById(id);
			latch.countDown();
		}
	}

}