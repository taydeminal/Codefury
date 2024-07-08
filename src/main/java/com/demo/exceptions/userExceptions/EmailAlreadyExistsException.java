package com.demo.exceptions.userExceptions;

public class EmailAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super();
	}

	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
