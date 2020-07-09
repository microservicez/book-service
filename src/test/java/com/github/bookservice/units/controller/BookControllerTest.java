package com.github.bookservice.units.controller;

import static com.github.bookservice.constants.BookServiceTestConstants.BASE_URL;
import static com.github.bookservice.constants.BookServiceTestConstants.BOOK_ID;
import static com.github.bookservice.constants.BookServiceTestConstants.BOOK_WITH_ID_1_NOT_FOUND;
import static com.github.bookservice.constants.BookServiceTestConstants.CLEAN_CODE_BOOK;
import static com.github.bookservice.constants.BookServiceTestConstants.CLEAN_DESIGN_BOOK;
import static com.github.bookservice.constants.BookServiceTestConstants.UTF8;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bookservice.controllers.BookController;
import com.github.bookservice.dto.Book;
import com.github.bookservice.exception.BookNotFoundException;
import com.github.bookservice.service.BookService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
  @Autowired private ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService service;

  @Test
  public void addUser_shouldAddUser() throws Exception {
    Book book = new Book();
    book.setName(CLEAN_CODE_BOOK);

    Book persistedBook = new Book();
    persistedBook.setName(CLEAN_CODE_BOOK);
    persistedBook.setId(1);

    given(service.addBook(book)).willReturn(persistedBook);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF8)
                .content(mapper.writeValueAsString(book))
                .accept(APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(persistedBook.getId()))
        .andExpect(jsonPath("name").value(persistedBook.getName()));
  }

  @Test
  public void getBook_ShouldReturnBook() throws Exception {
    Book book = new Book();
    book.setId(BOOK_ID);
    book.setName(CLEAN_CODE_BOOK);
    given(service.getBookById(BOOK_ID)).willReturn(book);

    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL + BOOK_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(BOOK_ID))
        .andExpect(jsonPath("name").value(book.getName()));
  }

  @Test
  public void getUser_shouldThowUserNotFoundException() throws Exception {
    given(service.getBookById(BOOK_ID)).willThrow(new BookNotFoundException(1));

    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL + BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
  }

  @Test
  public void getUsers_shouldReturnUserList() throws Exception {
    Book bookOne = new Book();
    Book bookTwo = new Book();
    bookOne.setId(1);
    bookOne.setName(CLEAN_CODE_BOOK);
    bookTwo.setId(2);
    bookTwo.setName(CLEAN_DESIGN_BOOK);
    List<Book> books = List.of(bookOne, bookTwo);
    given(service.getBookList()).willReturn(books);

    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is(CLEAN_CODE_BOOK)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is(CLEAN_DESIGN_BOOK)));
  }

  @Test
  public void getUsers_shouldReturnEmptyResult() throws Exception {
    given(service.getBookList()).willReturn(Collections.emptyList());
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void removeUser_shouldRemoveUser() throws Exception {
    doAnswer(x -> null).when(service).removeBookById(BOOK_ID);
    mockMvc
        .perform(MockMvcRequestBuilders.delete(BASE_URL + BOOK_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  public void removeUser_shouldThrowUserNotFoundException() throws Exception {
    doThrow(new BookNotFoundException(1)).when(service).removeBookById(1);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(BASE_URL + BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
  }

  @Test
  public void modifyBook_shouldEditTheBook() throws Exception {
    Book newBook = new Book();
    newBook.setId(BOOK_ID);
    newBook.setName(CLEAN_CODE_BOOK);
    given(service.modifyBook(newBook)).willReturn(newBook);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URL)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF8)
                .content(mapper.writeValueAsString(newBook))
                .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(newBook.getId()))
        .andExpect(jsonPath("name").value(newBook.getName()));
  }

  @Test
  public void modifyBook_shouldThrowBookNotFoundException() throws Exception {
    Book newBook = new Book();
    newBook.setId(BOOK_ID);
    newBook.setName(CLEAN_CODE_BOOK);
    given(service.modifyBook(newBook)).willThrow(new BookNotFoundException(1));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URL)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF8)
                .content(mapper.writeValueAsString(newBook))
                .accept(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
  }
}
