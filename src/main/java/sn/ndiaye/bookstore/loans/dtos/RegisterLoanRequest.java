package sn.ndiaye.bookstore.loans.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RegisterLoanRequest {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    private Integer durationInDays;
}
