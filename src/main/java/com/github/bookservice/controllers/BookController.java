package com.github.bookservice.controllers;

import com.github.bookservice.dto.Book;
import com.github.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService service;

    @Autowired
    BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/ping")
    public String isWorking() {
        return "pong";
    }

    @PostMapping()
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addBook(book));
    }

    @GetMapping()
    public ResponseEntity<Object> getAllBooks() {
        List<Book> books = service.getBookList();
        return CollectionUtils.isEmpty(books) ?
                ResponseEntity.ok().build() :
                ResponseEntity.ok(books);
    }

    @GetMapping("{book_id}")
    public ResponseEntity<Object> getBookByIsbn(@PathVariable("book_id") Integer bookId) {
        Book book = service.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping()
    public ResponseEntity<Object> modifyBook(@RequestBody Book book) {
        Book persistedBook = service.modifyBook(book);
        if (persistedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persistedBook);
    }

    @DeleteMapping("{book_id}")
    public ResponseEntity<Object> removeBook(@PathVariable("book_id") Integer id) {
        if (service.removeBookById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByIsbn")
    public ResponseEntity<Object> getBookByIsbn(@RequestParam("isbn") String isbn) {
        Book book = service.getBookByIsbn(isbn);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/booksByName/{name}")
    public ResponseEntity<Object> getBook(@PathVariable String name) {
        Book book = service.getBookByName(name);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }
}
