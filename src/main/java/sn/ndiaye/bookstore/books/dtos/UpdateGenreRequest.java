package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateGenreRequest {
    @NotBlank
    private String name;
}
