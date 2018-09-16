package com.demo.dto;

import java.io.Serializable;

public class Part implements Serializable {

	private static final long serialVersionUID = 8468357224321397247L;

	private long partId;
	private String make;
	private String model;
	private int year;

	public Part(String make, String model, int year) {
		this.make = make;
		this.model = model;
		this.year = year;
	}

	/**
	 * Getters Setters
	 */
	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}

	public int getYear() {
		return year;
	}

	public String getSearchKey() {
		return ("make:"+ make + ".model:" + model + ".year:" + year).toString();
	}
}
