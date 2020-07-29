package com.github.bookservice.dto;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(generator="book_id")
    @SequenceGenerator(name="book_id",sequenceName="book_id_seq", allocationSize=1)
    @ApiModelProperty(notes = "id of the book (Unique)")
    Integer id;
    
    @ApiModelProperty(notes = "isbn of the book (Unique)")
    String isbn;
    
    @ApiModelProperty(notes = "name of the book")
    String name;
    
    @ApiModelProperty(notes = "name of the book author")
    String authorName;
    
    @ApiModelProperty(notes = "genre of the book")
    String category;
    
    @ApiModelProperty(notes = "a short description of the book")
    String description;
    
    @ApiModelProperty(notes = "cost of the book")
    BigDecimal price;
}
