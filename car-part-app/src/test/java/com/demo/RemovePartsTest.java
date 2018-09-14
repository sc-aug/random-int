package com.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.Assert.assertTrue;

public class RemovePartsTest {

    private PartRepository partRepo;
    private int N;

    /**
     * Propergate DB with random data
     */
    @Before
    public void setup() {
        partRepo = new PartRepository(1L, new ConcurrentHashMap<>());

        N = 100;

        for (int i = 1; i <= N; i++) {
            final Part p = new Part("toyota", "camry", 2001);
            new Thread(() -> {
                partRepo.add(p);
            }).start();
        }
    }

    @Test
    public void inputData() {
        List<Long> randomIds = LongStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
        Collections.shuffle(randomIds);

        int deleteNum = 30;
        for (int i = 0; i < deleteNum; i ++) {
            partRepo.remove(randomIds.get(i));
        }

        assertTrue(N - deleteNum == partRepo.size());
    }
}
