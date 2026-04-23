package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterGenreRequest {
    @NotBlank
    private String name;
}
