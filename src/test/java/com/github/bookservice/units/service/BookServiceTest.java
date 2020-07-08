package com.github.bookservice.units.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.bookservice.dto.Book;
import com.github.bookservice.exception.BookNotFoundException;
import com.github.bookservice.repository.BookRepository;
import com.github.bookservice.service.BookService;



@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	@Mock
	private BookRepository repository;
	
	private BookService service;
	
	private static final Integer BOOK_ID = 1;
	
	@BeforeEach
	public void test() {
		service = new BookService(repository);
	}
	
	@Test
	public void addBook_shouldReturnBook() {
		given(repository.save(Mockito.any(Book.class))).willReturn(new Book());
		Book Book = service.addBook(new Book());
		assertThat(Book).isNotNull();
	}
	
	@Test
	public void getBook_returnsBook() {
		given(repository.findById(Mockito.anyInt())).willReturn(Optional.of(new Book()));
		Book Book = service.getBookById(Mockito.anyInt());
		assertThat(Book).isNotNull();
	}
	
	@Test
	public void getBook_throwsBookNotFoundExcpetion() {
		given(repository.findById(Mockito.anyInt())).willThrow(BookNotFoundException.class);
		assertThrows(BookNotFoundException.class, () -> service.getBookById(Mockito.anyInt()));
	}

	@Test
	public void getAllBooks_returnsAllBooks() {
		Book Book = new Book();
		Book.setId(1);
		Book.setName("John");
		List<Book> Books = List.of(Book);
		given(repository.findAll()).willReturn(Books);
		List<Book> allBooks = service.getBookList();
		
		assertThat(allBooks.size()).isEqualTo(1);
		assertThat(allBooks.get(0).getId()).isEqualTo(1);
		assertThat(allBooks.get(0).getName()).isEqualTo("John");
	}
	
	@Test
	public void modifyBook_modifiesBook() {
		Book oldBook = new Book();
		Book newBook = new Book();
		oldBook.setId(BOOK_ID);
		oldBook.setName("Clean Code");
		newBook.setId(BOOK_ID);
		newBook.setName("Clean Design");
		
		given(repository.findById(1)).willReturn(Optional.of(oldBook));
		given(repository.save(newBook)).willReturn(newBook);
		
		Book modifiedBook = service.modifyBook(newBook);
		assertThat(modifiedBook.getId()).isEqualTo(newBook.getId());
		assertThat(modifiedBook.getName()).isEqualTo(newBook.getName());
	}
	
	@Test
	public void modifyBook_throwsBookNotFoundException() {
		Book newBook = new Book();
		newBook.setId(BOOK_ID);
		newBook.setName("Clean Code");
		
		given(repository.findById(Mockito.anyInt())).willThrow(BookNotFoundException.class);
		assertThrows(BookNotFoundException.class, () -> service.modifyBook(newBook));
	}
}
