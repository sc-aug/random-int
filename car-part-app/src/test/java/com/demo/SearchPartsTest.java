package com.demo;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class SearchPartsTest {

    private PartRepository partRepo;

    /**
     * Setup 3 different type of car parts
     *      40 of [ toyota camry 2001 ]
     *      30 of [ toyota corolla 2000 ]
     *      30 of [ honda civic 2001 ]
     */
    @Before
    public void setup() {
        partRepo = new PartRepository(1L, new ConcurrentHashMap<>());

        int N = 100;

        for (int i = 1; i <= N; i++) {
            Part p = null;
            if (i > 60) {
                new Thread(() -> {
                    partRepo.add(new Part("toyota", "camry", 2001));
                }).start();
            } else if (i > 30) {
                new Thread(() -> {
                    partRepo.add(new Part("toyota", "corolla", 2000));
                }).start();
            } else {
                new Thread(() -> {
                    partRepo.add(new Part("honda", "civic", 2001));
                }).start();
            }

        }
    }

    @Test
    public void testSearchOnMake() {
        List<Part> pList = partRepo.search(new Search("toyota", "", -1));
        assertEquals(70, pList.size());
    }

    @Test
    public void testSearchOnModel() {
        List<Part> pList = partRepo.search(new Search(null, "CAMRY", -1));
        assertEquals(40, pList.size());
    }

    @Test
    public void testSearchOnYear() {
        List<Part> pList = partRepo.search(new Search(null, "", 2001));
        assertEquals(70, pList.size());
    }

    @Test
    public void testSearchOnMultipleCriterias1() {
        List<Part> pList = partRepo.search(new Search(null, "civiC", 2001));
        assertEquals(30, pList.size());
    }

    @Test
    public void testSearchOnMultipleCriterias2() {
        List<Part> pList = partRepo.search(new Search("toyota", "civiC", 2001));
        assertEquals(0, pList.size());
    }

}
