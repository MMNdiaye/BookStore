package sn.ndiaye.bookstore.books.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterLoanRequest {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    private Integer durationInDays;
}
