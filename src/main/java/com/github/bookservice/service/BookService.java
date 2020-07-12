package com.github.bookservice.service;

import com.github.bookservice.dto.Book;
import com.github.bookservice.exception.BookNotFoundException;
import com.github.bookservice.repository.BookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository repository;

  @Autowired
  public BookService(BookRepository repository) {
    this.repository = repository;
  }

  public List<Book> getBookList() {
    return repository.findAll();
  }

  public Book getBookById(Integer id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public Book addBook(Book book) {
    return repository.save(book);
  }

  public Book modifyBook(Book book) {
    Book oldBook =
        repository
            .findById(book.getId())
            .orElseThrow(() -> new BookNotFoundException(book.getId()));

    book.setId(oldBook.getId());
    return repository.save(book);
  }

  public void removeBookById(Integer id) {
    Book oldBook = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

    repository.delete(oldBook);
  }
}
