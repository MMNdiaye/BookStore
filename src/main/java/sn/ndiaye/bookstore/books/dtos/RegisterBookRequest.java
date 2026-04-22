package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;
import java.util.Set;

@Data
public class RegisterBookRequest {
    @NotBlank
    @ISBN
    private String isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    @PastOrPresent
    private LocalDate publishedAt;

    @Max(value = Long.MAX_VALUE)
    private Long quantity = 1L;

    @Size(min = 1)
    private Set<String> genres;
}