package com.debate.vsys.core.service;

import java.util.Collection;

import com.debate.vsys.core.data.MotionDao;
import com.debate.vsys.model.Motion;

public class MotionServiceImpl implements MotionService {
	
	private MotionDao motionDao;
	
	public MotionServiceImpl(MotionDao motionDao) {
		this.motionDao = motionDao;
	}
	
	public Motion create(Motion motion) {
		return motionDao.create(motion);
	}
	
	public Motion find(String mId) {
		return motionDao.find(mId);
	}
	
	public Collection<Motion> findAll() {
		return motionDao.findAll();
	}
	
	public Iterable<Motion> findAllTied() {
		return motionDao.findAllTied();
	}
	
	public Motion update(Motion motion) {
		return motionDao.update(motion);
	}
	
	public boolean isTiedMotion(String mId) {
		return motionDao.isTiedMotion(mId);
	}
	
}
