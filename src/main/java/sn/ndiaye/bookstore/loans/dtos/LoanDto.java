package sn.ndiaye.bookstore.loans.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoanDto {
    private UUID id;
    private UUID userId;
    private Long bookId;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime takenAt;
    private Integer durationInDays;
    private Long remainingDays;
    private BigDecimal initialFee;
}
