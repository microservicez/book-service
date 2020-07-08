package com.github.bookservice.units.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bookservice.controllers.BookController;
import com.github.bookservice.dto.Book;
import com.github.bookservice.exception.BookNotFoundException;
import com.github.bookservice.service.BookService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
	private static final String UTF8 = "utf8";
	private static final String BOOK_WITH_ID_1_NOT_FOUND = "Book with book id: 1 not found";
	private static final String BASE_URL = "/api/v1/books/";
	private static final Integer BOOK_ID = 1;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookService service;
	
	@Test
	public void addUser_shouldAddUser() throws Exception {
		Book book = new Book();
		book.setName("Clean Code");
		
		Book persistedBook = new Book();
		persistedBook.setName("Clean Code");
		persistedBook.setId(1);
		
		given(service.addBook(book)).willReturn(persistedBook);
		
		mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				                              .contentType(MediaType.APPLICATION_JSON)
				                              .characterEncoding(UTF8)
				                              .content(mapper.writeValueAsString(book)).accept(MediaType.APPLICATION_JSON))
											 
		       .andExpect(status().isCreated())
		       .andExpect(jsonPath("id").value(persistedBook.getId()))
		       .andExpect(jsonPath("name").value(persistedBook.getName()));
	}
	
	@Test
	public void getBook_ShouldReturnBook() throws Exception {
		Book book = new Book();
		book.setId(BOOK_ID);
		book.setName("Clean Code");
		given(service.getBookById(BOOK_ID)).willReturn(book);
		
		mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+BOOK_ID))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("id").value(BOOK_ID))
		       .andExpect(jsonPath("name").value(book.getName()));
	}
	
	@Test
	public void getUser_shouldThowUserNotFoundException() throws Exception {
		given(service.getBookById(BOOK_ID)).willThrow(new BookNotFoundException(1));
		
		mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+BOOK_ID))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
	}
	
	@Test
	public void getUsers_shouldReturnUserList() throws Exception {
		Book bookOne = new Book();
		Book bookTwo = new Book();
		bookOne.setId(1);
		bookOne.setName("Clean Code");
		bookTwo.setId(2);
		bookTwo.setName("Clean Design");
		List<Book> books = List.of(bookOne, bookTwo);
		given(service.getBookList()).willReturn(books);
		
		mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
	       	   .andExpect(status().isOk())
	           .andExpect(jsonPath("$[0].id", is(1)))
	    	   .andExpect(jsonPath("$[0].name", is("Clean Code")))
	    	   .andExpect(jsonPath("$[1].id", is(2)))
	           .andExpect(jsonPath("$[1].name", is("Clean Design")));
	}
	
	@Test
	public void getUsers_shouldReturnEmptyResult() throws Exception {
		given(service.getBookList()).willReturn(Collections.emptyList());
		mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
    	   .andExpect(status().isOk())
    	   .andExpect(jsonPath("$").doesNotExist());
	}
	
	@Test
	public void removeUser_shouldRemoveUser() throws Exception {
		doAnswer(x -> null).when(service).removeBookById(BOOK_ID);
		mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL+BOOK_ID))
	       .andExpect(status().isNoContent());
	}
	
	@Test
	public void removeUser_shouldThrowUserNotFoundException() throws Exception{
		doThrow(new BookNotFoundException(1)).when(service).removeBookById(1);
		
		mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL+BOOK_ID))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
	}
	
	@Test
	public void modifyBook_shouldEditTheBook() throws Exception {
		Book newBook = new Book();
		newBook.setId(BOOK_ID);
		newBook.setName("Clean Code");
		given(service.modifyBook(newBook)).willReturn(newBook);
		
		mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(UTF8)
					.content(mapper.writeValueAsString(newBook))
					.accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("id").value(newBook.getId()))
		       .andExpect(jsonPath("name").value(newBook.getName()));
	}
	
	@Test
	public void modifyBook_shouldThrowBookNotFoundException() throws Exception {
		Book newBook = new Book();
		newBook.setId(BOOK_ID);
		newBook.setName("Clean Code");
		given(service.modifyBook(newBook)).willThrow(new BookNotFoundException(1));
		
		mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(UTF8)
					.content(mapper.writeValueAsString(newBook))
					.accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("errorMessage").value(BOOK_WITH_ID_1_NOT_FOUND));
	}
}
