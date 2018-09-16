package com.demo;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.service.PartService;
import com.demo.service.PartServiceImpl;

public class SearchPartsTest {

	private static PartService service;

	/**
	 * Setup 3 different type of car parts
	 *      40 of [ toyota camry 2001 ]
	 *      30 of [ toyota corolla 2000 ]
	 *      30 of [ honda civic 2001 ]
	 * @throws InterruptedException 
	 */
	@BeforeClass
	public static void setup() throws InterruptedException {
		service = new PartServiceImpl();

		int N = 100;
		ExecutorService ex= Executors.newCachedThreadPool();
		CountDownLatch latch= new CountDownLatch(100);
		
		for(int i = 61; i <= N; i ++) {
			ex.execute(() -> {
				try {
					service.addPart(new Part("toyota", "camry", 2001));
					latch.countDown();
				} catch (InvalidPartException e) {
					e.printStackTrace();
				}
			});
		}
		for (int i = 31; i<=60; i++) {
			ex.execute(()-> {
				try {
					service.addPart(new Part("toyota", "corolla", 2000));
					latch.countDown();
				} catch (InvalidPartException e) {
					e.printStackTrace();
				}
			});
		}
		for(int i = 1; i <= 30; i ++) {
			ex.execute(() -> {
				try {
					service.addPart(new Part("honda", "civic", 2001));
					latch.countDown();
				} catch (InvalidPartException e) {
					e.printStackTrace();
				}
			});
		}
		latch.await(2000, TimeUnit.MILLISECONDS);
	}
	

    @Test
    public void testSearchOnMake() {
        List<Part> pList = service.searchPartByMake("toyota");
        assertEquals(70, pList.size());
    }

    @Test
    public void testSearchOnModel() {
        List<Part> pList = service.searchPartByModel("camry");
        assertEquals(40, pList.size());
    }

    @Test
    public void testSearchOnYear() {
        List<Part> pList = service.searchPartByYear(2001);
        assertEquals(70, pList.size());
    }

    @Test
    public void testSearchOnMultipleCriterias1() {
        List<Part> pList = service.searchPart("honda", "civic", 2001);
        assertEquals(30, pList.size());
    }

    @Test
    public void testSearchOnMultipleCriterias2() {
    		List<Part> pList = service.searchPart("toyota", "", -1);
        assertEquals(70, pList.size());
    }
}
