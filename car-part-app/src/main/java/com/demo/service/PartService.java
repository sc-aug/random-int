package com.demo.service;

import java.util.List;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;

public interface PartService {

	public long addPart(Part p) throws InvalidPartException;

	public Part removePartById(long partId);

	public List<Part> searchPartByMake(String make);
	
	public List<Part> searchPartByModel(String model);

	public List<Part> searchPartByYear(int year);
	
	public List<Part> searchPart(String model, String make, int year);

	public Part searchPartById(long id);

	public long getNumberOfParts();
}
