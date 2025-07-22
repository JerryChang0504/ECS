package com.giun.ecs.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -5612351470382473938L;

	public ApplicationException(String message) {
		super(message);
	}

}
