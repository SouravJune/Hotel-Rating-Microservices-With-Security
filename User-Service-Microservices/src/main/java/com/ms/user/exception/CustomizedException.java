package com.ms.user.exception;

import org.springframework.http.HttpStatus;

public class CustomizedException extends RuntimeException{
	private HttpStatus status;
	private String message;
	
	public CustomizedException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public CustomizedException(String message) {
		super(message);
	}
	
	public CustomizedException() {
		// TODO Auto-generated constructor stub
	}

	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
