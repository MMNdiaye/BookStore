package sn.ndiaye.bookstore.payments.entities;

import jakarta.persistence.*;
import lombok.*;
import sn.ndiaye.bookstore.loans.entities.Loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_payments")
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Column(name = "cost")
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private LoanPaymentReason reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
