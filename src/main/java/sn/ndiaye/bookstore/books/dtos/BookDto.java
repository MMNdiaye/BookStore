package sn.ndiaye.bookstore.books.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Set<GenreDto> genres;
}
