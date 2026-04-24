package sn.ndiaye.bookstore.books.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoanDto {
    private UUID userId;
    private Long bookId;
    private LocalDateTime takenAt;
    private Integer durationInDays;
    private Long remainingDays;
    private BigDecimal initialFee;
}
