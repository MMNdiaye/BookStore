package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.payments.entities.PaymentType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Book book;
    private PaymentType paymentType;
    private String operationId;
    private BigDecimal unitCost;
    private Long quantity;


    public String getBookName() {
        return book.getTitle() + "-" + book.getAuthor();
    }

    public String getType() {
        return paymentType.name();
    }

    public static PaymentRequest initialLoanPayment(Loan loan) {
        return PaymentRequest.builder()
                .book(loan.getBook())
                .paymentType(PaymentType.LOAN)
                .operationId(loan.getId().toString())
                .quantity(1L)
                .unitCost(loan.processInitialFee())
                .build();
    }

    public static PaymentRequest returnLoanPenality(Loan loan) {
        return PaymentRequest.builder()
                .book(loan.getBook())
                .paymentType(PaymentType.PENALITY)
                .operationId(loan.getId().toString())
                .quantity(1L)
                .unitCost(loan.processReturnFee().multiply(BigDecimal.valueOf(-1)))
                .build();
    }
}
