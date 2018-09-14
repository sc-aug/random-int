package com.demo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddPartsTest {

    private PartRepository partRepo;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        partRepo = new PartRepository(1L, new ConcurrentHashMap<>());
    }

    /**
     * test if the generated partId equals to updated partId
     */
    @Test
    public void testAddPart() {
        Part part = new Part("Toyota", "Corolla", 2000);
        long partId = partRepo.add(part);
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
        ExecutorService ex = Executors.newCachedThreadPool();

        for (int i = 0; i < N; i++) {
            ex.submit(() -> {
                Thread.sleep(random.nextInt(TIME));
                partRepo.add(new Part("Foo", "Bar", 2000));
                countDownLatch.countDown();
                return null;
            });
        }

        countDownLatch.await(1, TimeUnit.MINUTES);

        assertTrue(N == this.partRepo.size());
    }

}
