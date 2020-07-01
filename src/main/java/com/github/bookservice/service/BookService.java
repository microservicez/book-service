package com.github.bookservice.service;

import com.github.bookservice.dto.Book;
import com.github.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;
    @Autowired
    BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBookList() {
         return repository.findAll();
    }

    public Book getBookById(Integer id) {
        return repository.findById(id).orElse(null);
    }
    public Book getBookByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public Book getBookByIsbn(String isbn) {
        return repository.findByIsbn(isbn).orElse(null);
    }

    public Book addBook(Book book) {
         return repository.save(book);
    }

    public Book modifyBook(Book book) {
        Optional<Book> oldBook = repository.findById(book.getId());
        if(oldBook.isEmpty()) {
            return null;
        }
        book.setId(oldBook.get().getId());
        return repository.save(book);
    }

    public Boolean removeBookById(Integer id) {
        Optional<Book> oldBook = repository.findById(id);
        if (oldBook.isEmpty()) {
            return false;
        }
        repository.delete(oldBook.get());
        return true;
    }

    public Boolean removeBookByISBN(String isbn) {
        Optional<Book> oldBook = repository.findByIsbn(isbn);
        if (oldBook.isEmpty()) {
            return false;
        }
        repository.delete(oldBook.get());
        return true;
    }
}
