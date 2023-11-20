package com.ms.hotel.exception_handler;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ms.hotel.exception.CannotDeleteException;
import com.ms.hotel.exception.CannotUpdateException;
import com.ms.hotel.exception.ResourceNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ProblemDetail> handlerResourceNotFoundException(ResourceNotFoundException exception){
		
		ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_ACCEPTABLE);
		problemDetails.setTitle("ResourceNotFoundException");
		problemDetails.setDetail(exception.getMessage());
		problemDetails.setType(URI.create("http://localhost:8082/api/v1/errors/resource-not-found"));
		problemDetails.setStatus(HttpStatus.NOT_FOUND);
		problemDetails.setProperty("timestamp", LocalDateTime.now());
		problemDetails.setProperty("port", 8082);
		problemDetails.setProperty("host", "localhost");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(problemDetails);
	}
	
	@ExceptionHandler(CannotUpdateException.class)
	public ResponseEntity<ProblemDetail> handlerCannotUpdateException(CannotUpdateException exception){
		
		ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_ACCEPTABLE);
		problemDetails.setTitle("CannotUpdateException");
		problemDetails.setDetail(exception.getMessage());
		problemDetails.setType(URI.create("http://localhost:8082/api/v1/errors/cannot-update"));
		problemDetails.setStatus(HttpStatus.NOT_FOUND);
		problemDetails.setProperty("timestamp", LocalDateTime.now());
		problemDetails.setProperty("port", 8082);
		problemDetails.setProperty("host", "localhost");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(problemDetails);
	}
	
	@ExceptionHandler(CannotDeleteException.class)
	public ResponseEntity<ProblemDetail> handlerCannotDeleteException(CannotDeleteException exception){
		
		ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_ACCEPTABLE);
		problemDetails.setTitle("CannotDeleteException");
		problemDetails.setDetail(exception.getMessage());
		problemDetails.setType(URI.create("http://localhost:8082/api/v1/errors/cannot-delete"));
		problemDetails.setStatus(HttpStatus.NOT_FOUND);
		problemDetails.setProperty("timestamp", LocalDateTime.now());
		problemDetails.setProperty("port", 8082);
		problemDetails.setProperty("host", "localhost");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(problemDetails);
	}
	
}
