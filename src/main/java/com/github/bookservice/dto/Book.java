package com.github.bookservice.dto;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(generator="book_id")
    @SequenceGenerator(name="book_id",sequenceName="book_id_seq", allocationSize=1)
    Integer id;
    String isbn;
    String name;
    String authorName;
    String category;
    String description;
    BigDecimal price;
}
