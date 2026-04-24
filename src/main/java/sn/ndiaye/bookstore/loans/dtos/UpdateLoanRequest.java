package sn.ndiaye.bookstore.loans.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateLoanRequest {
    @PastOrPresent
    private LocalDateTime takenAt;
    @Min(1)
    private Integer durationInDays;
}
