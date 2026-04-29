package sn.ndiaye.bookstore.loans.entities;

import jakarta.persistence.*;
import lombok.*;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.users.entities.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "duration_in_days")
    private Integer durationInDays;

    @Column(name = "rate_per_day")
    private BigDecimal ratePerDay;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public BigDecimal processInitialFee() {
        return ratePerDay.multiply(BigDecimal.valueOf(durationInDays));
    }

    public BigDecimal processReturnFee() {
        var initialFee = processInitialFee();
        if (getRemainingDays() == 0)
            return BigDecimal.ZERO;
        
        // pay > 0 means the loan is returned early, pay < 0 means the loan is returned late
        var remainingDaysPay = ratePerDay.multiply(BigDecimal.valueOf(getRemainingDays()));
        return initialFee.subtract(remainingDaysPay);
    }

    public long getRemainingDays() {
        var elapsedDays = Duration.between(takenAt, LocalDateTime.now())
                .toDays();
        return durationInDays - elapsedDays;
    }

    public boolean isDue() {
        return getRemainingDays() <= 0;
    }
}
