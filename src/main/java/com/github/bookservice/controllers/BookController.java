package com.github.bookservice.controllers;

import com.github.bookservice.dto.Book;
import com.github.bookservice.service.BookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
  private final BookService service;

  @Autowired
  BookController(BookService service) {
    this.service = service;
  }

  @PostMapping()
  @ApiOperation(value = "Adds Book")
  public ResponseEntity<Book> addBook(@RequestBody Book book) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.addBook(book));
  }

  @GetMapping()
  @ApiOperation(value = "Gets All the Book")
  public ResponseEntity<Object> getAllBooks() {
    List<Book> books = service.getBookList();
    return CollectionUtils.isEmpty(books) ? ResponseEntity.ok().build() : ResponseEntity.ok(books);
  }

  @GetMapping("{book_id}")
  @ApiOperation(value = "Retreives book on the basis of id")
  public ResponseEntity<Object> getBookById(@ApiParam(value = "id of the book to be retrieved", required = true) 
  	@PathVariable("book_id") Integer bookId) {
    return ResponseEntity.ok(service.getBookById(bookId));
  }

  @PutMapping()
  @ApiOperation(value = "Modifies book on the basis entity Book")
  public ResponseEntity<Object> modifyBook(@ApiParam(value = "Book entity that needs to be modified", required = true) 
  	@RequestBody Book book) {
    return ResponseEntity.ok(service.modifyBook(book));
  }

  @DeleteMapping("{book_id}")
  @ApiOperation(value = "Deletes book on the basis of id")
  public ResponseEntity<Object> removeBook(@ApiParam(value = "id of the book to be deleted", required = true) 
  	@PathVariable("book_id") Integer id) {
    service.removeBookById(id);
    return ResponseEntity.noContent().build();
  }
}
