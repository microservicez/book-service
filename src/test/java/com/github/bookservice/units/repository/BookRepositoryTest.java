package com.github.bookservice.units.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.bookservice.dto.Book;
import com.github.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {
  private final BookRepository repository;
  private final TestEntityManager entityManager;

  @Autowired
  BookRepositoryTest(BookRepository repository, TestEntityManager entityManager) {
    this.repository = repository;
    this.entityManager = entityManager;
  }

  @Test
  public void getBook_returnsBook() {
    Book Book = new Book();
    Book.setName("Clean Code");
    Book persistedBook = entityManager.persistFlushFind(Book);
    Book BookLookUp = repository.findById(persistedBook.getId()).get();

    assertThat(BookLookUp.getId()).isEqualTo(persistedBook.getId());
    assertThat(BookLookUp.getName()).isEqualTo(persistedBook.getName());
  }

  @Test
  public void addBook_persistBook() {
    Book Book = new Book();
    Book.setName("CleanCode");
    Book persistedBook = repository.save(Book);

    assertThat(persistedBook.getName()).isEqualTo(Book.getName());
  }
}
