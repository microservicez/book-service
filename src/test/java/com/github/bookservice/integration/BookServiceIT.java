package com.github.bookservice.integration;

import static com.github.bookservice.constants.BookServiceTestConstants.BASE_URL;
import static com.github.bookservice.constants.BookServiceTestConstants.BOOK_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.bookservice.dto.Book;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceIT {
  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  public void createBook() throws Exception {
    Book book = new Book();
    book.setName("Clean Code");
    ResponseEntity<Book> response = restTemplate.postForEntity(BASE_URL, book, Book.class);
    Book persistedBook = response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(persistedBook.getId()).isEqualTo(1);
    assertThat(persistedBook.getName()).isEqualTo(book.getName());
  }

  @Test
  @Order(2)
  public void getAllBooks() throws Exception {
    ResponseEntity<Object> response = restTemplate.getForEntity(BASE_URL, Object.class);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @Order(3)
  public void getBook() throws Exception {
    ResponseEntity<Book> response = restTemplate.getForEntity(BASE_URL + BOOK_ID, Book.class);
    Book book = response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(book.getId()).isEqualTo(1);
    assertThat(book.getName()).isEqualTo("Clean Code");
  }

  @Test
  @Order(4)
  public void modifyBook() throws Exception {
    Book book = new Book();
    book.setId(1);
    book.setName("Clean Design");
    ResponseEntity<Book> response =
        restTemplate.exchange(BASE_URL, HttpMethod.PUT, new HttpEntity<Book>(book), Book.class);
    Book persistedBook = response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(persistedBook.getId()).isEqualTo(1);
    assertThat(persistedBook.getName()).isEqualTo(book.getName());
  }

  @Test
  @Order(5)
  public void removeBook() throws Exception {
    ResponseEntity<Object> response =
        restTemplate.exchange(BASE_URL + BOOK_ID, HttpMethod.DELETE, null, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  public void removeBook_throwsBookNotFoundExcpetion() throws Exception {
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL + BOOK_ID, HttpMethod.DELETE, null, String.class);
    assertBookNotFoundScenario(response);
  }

  @Test
  public void getBook_throwsBookNotFoundExcpetion() throws Exception {
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL + BOOK_ID, HttpMethod.GET, null, String.class);
    assertBookNotFoundScenario(response);
  }

  @Test
  public void modifyBook_throwsBookNotFoundExcpetion() throws Exception {
    Book book = new Book();
    book.setId(1);
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL, HttpMethod.PUT, new HttpEntity<Book>(book), String.class);
    assertBookNotFoundScenario(response);
  }

  private void assertBookNotFoundScenario(ResponseEntity<String> response) throws Exception {
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    JSONObject json = new JSONObject(response.getBody());

    assertThat(json.get("errorMessage")).isEqualTo("Book with book id: 1 not found");
  }
}
