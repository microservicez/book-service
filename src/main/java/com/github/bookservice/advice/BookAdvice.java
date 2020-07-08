package com.github.bookservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.bookservice.exception.BookNotFoundException;

@ControllerAdvice
public class BookAdvice {
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<Object> handleBookNotFoundExcpetion(BookNotFoundException bookNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body("{\"errorMessage\":\""+bookNotFoundException.getMessage() +"\"}");
	}
}
