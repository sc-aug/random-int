package com.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.demo.exception.DuplicatedKeyException;

public class PartRepository {

    private static long maxId;

    private static Object idLock;

    private static ConcurrentHashMap<Long, Part> partsDb;

    public PartRepository(long mId, ConcurrentHashMap<Long, Part> map) {
        maxId = mId;
        partsDb = map;
        idLock = new Object();
    }

    public long add(Part p) {
        if (p == null) { // failed
            return -1;
        }
        long partId = generateId();
        if (partsDb.containsKey(partId)) {
            throw new DuplicatedKeyException("add(Part p). duplicated key generated: " + partId);
        }
        p.setPartId(partId);
        partsDb.put(partId, p);
        return p.getPartId();
    }

    public Part remove(long partId) {
        if (partsDb.containsKey(partId)) {
            return partsDb.remove(partId);
        }
        return null;
    }

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

    public long size() {
        return partsDb.size();
    }

    private long generateId() {
        long id;
        synchronized (idLock) {
            id = maxId;
            maxId ++;
        }
        return id;
    }

}
