package com.project.os.exceptionhandler;

public class ProcessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2103317993695490421L;

	public ProcessException() {
	}
	
	public ProcessException(String message) {
		super(message);
	}
	
	public ProcessException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
