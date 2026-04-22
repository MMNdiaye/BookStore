package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterPublisherRequest{
    @NotBlank
    private String name;
}
