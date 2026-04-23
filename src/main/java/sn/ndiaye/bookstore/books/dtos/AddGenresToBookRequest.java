package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AddGenresToBookRequest {
    @Size(min = 1)
    private Set<String> genres;
}
