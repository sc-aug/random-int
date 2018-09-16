package com.demo.service;

import java.util.List;

import com.demo.dao.PartRepository;
import com.demo.dao.PartRepositoryImpl;
import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.util.Search;

public class PartServiceImpl implements PartService {
	
	private PartRepository repository;
	
	{
		this.repository= new PartRepositoryImpl();
	}
	
	@Override
	public long addPart(Part p) throws InvalidPartException {
		return repository.add(p);
	}

	@Override
	public Part removePartById(long partId) {
		return repository.remove(partId);
	}

	@Override
	public List<Part> searchPartByMake(String make) {
		return repository.searchByMake(make);
	}

	@Override
	public List<Part> searchPartByModel(String model) {
		return repository.searchByModel(model);
	}

	@Override
	public List<Part> searchPartByYear(int year) {
		return repository.searchByYear(year);
	}

	@Override
	public List<Part> searchPart(String model, String make, int year) {
		Search search = new Search(model, make, year);
		return repository.search(search);
	}

	@Override
	public Part searchPartById(long id) {
		return repository.searchById(id);
	}

	@Override
	public long getNumberOfParts() {
		return repository.size();
	}



}
