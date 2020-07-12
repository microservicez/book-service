package com.github.bookservice.exception;

public class BookNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BookNotFoundException(Integer id) {
    super("Book with book id: " + id + " not found");
  }
}
