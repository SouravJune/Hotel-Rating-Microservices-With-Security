package com.ms.hotel.exception;

public class CannotDeleteException extends RuntimeException{
	public CannotDeleteException(String message) {
		super(message);
	}

}
