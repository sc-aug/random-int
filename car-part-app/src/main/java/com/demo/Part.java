package com.demo;

public class Part {

    private long partId;
    private String make;
    private String model;
    private int year;
    private String searchKey;

    public Part(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.searchKey = "make:"+ make + ".model:" + model + ".year:" + year;
        this.searchKey = this.searchKey.toLowerCase();
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
        return searchKey;
    }
}
