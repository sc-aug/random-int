package com.demo.vsys.exception;

public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 4752980820040855474L;

	public InvalidStatusException(String msg) {
		super(msg);
	}

}
