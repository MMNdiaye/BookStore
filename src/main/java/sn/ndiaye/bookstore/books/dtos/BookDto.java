package sn.ndiaye.bookstore.books.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
}
