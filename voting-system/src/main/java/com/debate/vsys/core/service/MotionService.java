package com.debate.vsys.core.service;

import java.util.Collection;

import com.debate.vsys.model.Motion;

public interface MotionService {
	
	public Motion create(Motion motion);
	
	public Motion find(String mId);
	
	public Collection<Motion> findAll();
	
	public Iterable<Motion> findAllTied();
	
	public Motion update(Motion motion);
	
	public boolean isTiedMotion(String mId);
	
}
