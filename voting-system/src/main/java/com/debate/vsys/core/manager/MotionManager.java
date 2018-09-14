package com.debate.vsys.core.manager;

import java.time.LocalDateTime;
import java.util.Collection;

import com.debate.vsys.core.service.MotionService;
import com.debate.vsys.model.Motion;

public class MotionManager {
	
	private MotionService motionService;
	
	public MotionManager(MotionService ms) {
		this.motionService = ms;
	}
	
	/**
	 * Create motion
	 */
	public Motion create(String des, LocalDateTime st, LocalDateTime et) {
		Motion motion = new Motion(des, st, et);
		return motionService.create(motion);
	}
	
	/**
	 * Find a motion by id
	 */
	public Motion find(String mId) {
		return motionService.find(mId);
	}
	
	/**
	 * Get all the motions
	 */
	public Collection<Motion> findAll() {
		return motionService.findAll();
	}
	
	/**
	 * Get all tied motions
	 */
	public Iterable<Motion> findAllTied() {
		return motionService.findAllTied();
	}
	
	/**
	 * Update a motion
	 */
	public Motion update(Motion motion) {
		return motionService.update(motion);
	}
	
}
