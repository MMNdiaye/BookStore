package sn.ndiaye.bookstore.books;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class UpdateBookRequest {
    @ISBN
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    @Min(1L)
    private Long quantity;
}
