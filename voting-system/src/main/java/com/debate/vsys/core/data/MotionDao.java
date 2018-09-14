package com.debate.vsys.core.data;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.debate.vsys.core.config.CoreResources;
import com.debate.vsys.model.Motion;
import com.debate.vsys.model.MotionStatus;

/**
 * Motion crud operations
 */
public class MotionDao {
	/**
	 * Keep motion data
	 */
	private static ConcurrentHashMap<String, Motion> motionsDb;
	/**
	 * This queue racing the preactive/active motions.
	 * 
	 * The RUNNABLE/RUNNING/PENDING motion will be keeped
	 * The TIED/TERMINATED motion will be removed from the queue
	 */
	private static BlockingQueue<Motion> activeMotionsQueue;
	
	private static BlockingQueue<Motion> tiedMotionsQueue;
	
	public MotionDao(CoreResources res) {
		motionsDb = res.getMotionsDb();
		activeMotionsQueue = res.getActiveMotionsQueue();
		tiedMotionsQueue = res.getTiedMotionsQueue();
	}
	
	public Motion create(Motion motion) {
		motionsDb.put(motion.getMId(), motion);
		activeMotionsQueue.offer(motion);
		return motion;
	}
	
	public Motion find(String mId) {
		return motionsDb.get(mId);
	}
	
	public Collection<Motion> findAll() {
		return motionsDb.values();
	}
	
	public Iterable<Motion> findAllTied() {
		return tiedMotionsQueue;
	}
	
	public Motion update(Motion motion) {
		motionsDb.put(motion.getMId(), motion);
		return motion;
	}
	
	public boolean isTiedMotion(String mId) {
		return motionsDb.get(mId).getStatus() == MotionStatus.TIED;
	}
	
}
