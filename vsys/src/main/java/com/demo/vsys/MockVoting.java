package com.demo.vsys;

public class MockVoting implements Runnable {
	
	private long HESITATE_MIILIS;
	private Vote vote;
	
	public MockVoting(long h, Vote v) {
		this.HESITATE_MIILIS = h;
		this.vote = v;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(HESITATE_MIILIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.vote.getMotion().submit(vote);
	}

}
