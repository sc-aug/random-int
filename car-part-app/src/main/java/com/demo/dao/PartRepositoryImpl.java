package com.demo.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.demo.dto.Part;
import com.demo.exception.InvalidPartException;
import com.demo.util.Search;

public class PartRepositoryImpl implements PartRepository {

	private AtomicLong maxId = new AtomicLong();

	private  ConcurrentHashMap<Long, Part> partsDb = new ConcurrentHashMap<>();

	@Override
	public long add(Part p) throws InvalidPartException {
		if (p == null) {
			throw new InvalidPartException("add(Part p) method get invalid Part: " + p);
		}
		long partId = generateId();
		p.setPartId(partId);
		partsDb.put(partId, p);
		return partId;
	}

	@Override
	public Part remove(long partId) {
		return partsDb.remove(partId);
	}

	@Override
	public List<Part> searchByMake(String make) {
		return partsDb.values().stream().filter(x-> x.getMake().equals(make)).collect(Collectors.toList());
	}

	@Override
	public List<Part> searchByModel(String model) {
		return partsDb.values().stream().filter(x-> x.getModel().equals(model)).collect(Collectors.toList());
	}

	@Override
	public List<Part> searchByYear(int year) {
		return partsDb.values().stream().filter(x-> x.getYear() == year).collect(Collectors.toList());
	}

	@Override
	public List<Part> search(Search search) {
		List<Part> pList = new LinkedList<>();
		Pattern pattern = Pattern.compile(search.getSearchRegex());
		Matcher matcher;
		for (Part p: partsDb.values()) {
			matcher = pattern.matcher(p.getSearchKey());
			if (matcher.find()) {
				pList.add(p);
			}
		}
		return pList;
	}

	@Override
	public Part searchById(long id) {
		return partsDb.get(id);
	}

	@Override
	public long size() {
		return partsDb.size();
	}

	private long generateId() {
		return maxId.incrementAndGet();
	}

}
