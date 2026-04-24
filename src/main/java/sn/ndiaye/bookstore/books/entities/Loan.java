package sn.ndiaye.bookstore.books.entities;

import jakarta.persistence.*;
import lombok.*;
import sn.ndiaye.bookstore.users.entities.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
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

    @Column(name = "initial_fee")
    private BigDecimal initialFee;

    @Column(name = "rate_per_day")
    private BigDecimal ratePerDay;

    public BigDecimal processInitialFee() {
        return ratePerDay.multiply(BigDecimal.valueOf(durationInDays));
    }

    public long getRemainingDays() {
        var elapsedDays = Duration.between(takenAt, LocalDateTime.now())
                .toDays();
        return durationInDays - elapsedDays;
    }
}
