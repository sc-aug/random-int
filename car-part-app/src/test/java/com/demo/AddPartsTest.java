package com.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.service.PartService;
import com.demo.service.PartServiceImpl;

public class AddPartsTest {

	private static PartService service;

	@Before
	public void init() {
		service = new PartServiceImpl();
	}

	@Test
	public void testAddPart() throws InvalidPartException {
		Part part = new Part("Toyota", "Corolla", 2000);
		long partId = service.addPart(part);
		assertEquals(partId, part.getPartId());
	}

	/**
	 * Test thread save of add method.
	 * Adding N number of car parts in to db
	 *   - no ID conflict
	 *   - number of parts in db no less than N
	 */
	@Test
	public void testThreadSaveOnAdd() throws InterruptedException {
		int N = 10000;
		int TIME = 100;
		Random random = new Random();
		CountDownLatch countDownLatch = new CountDownLatch(N);
		Executor ex = Executors.newCachedThreadPool();

		for (int i = 0; i < N; i++) {
			ex.execute(() -> {
				try {
					Thread.sleep(random.nextInt(TIME));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					service.addPart(new Part("Foo", "Bar", 2000));
				} catch (InvalidPartException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			});
		}

		countDownLatch.await(1, TimeUnit.MINUTES);
		System.out.println(service.getNumberOfParts());
		assertTrue(N == this.service.getNumberOfParts());
	}

}
