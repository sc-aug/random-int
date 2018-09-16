package com.demo.dao;

import java.util.List;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.util.Search;

public interface PartRepository {

	public long add(Part p) throws InvalidPartException;

	public Part remove(long partId);

	public List<Part> searchByMake(String make);
	
	public List<Part> searchByModel(String model);

	public List<Part> searchByYear(int year);
	
	public List<Part> search(Search search);

	public Part searchById(long id);

	public long size();

}
