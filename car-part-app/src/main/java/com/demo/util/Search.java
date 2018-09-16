package com.demo.util;

public class Search {

    private String searchRegex;

    public Search(String make, String model, int year) {
        this.searchRegex = "^"
                + "make:.*"  +  (checkString(make) ?  (make + ".*") : "")
                + "model:.*" +  (checkString(model) ? (model + ".*") : "")
                + "year:"  +  (checkYear(year) ? (year + "$") : ".*$");
        this.searchRegex = this.searchRegex.toLowerCase();
    }

    private boolean checkString(String str) {
        if (str == null || str.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkYear(int year) {
        if (year > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getSearchRegex() {
        return searchRegex;
    }

}
