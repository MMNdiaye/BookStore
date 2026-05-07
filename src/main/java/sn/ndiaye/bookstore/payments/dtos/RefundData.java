package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.payments.entities.PaymentProvider;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
public class RefundData {
    private BigDecimal amount;
    private String refundId;
    private PaymentProvider provider;
}
