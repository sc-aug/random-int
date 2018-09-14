package com.demo.exception;

public class DuplicatedKeyException extends RuntimeException {
    public DuplicatedKeyException(String msg) {
        super(msg);
    }
}
