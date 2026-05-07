package sn.ndiaye.bookstore.payments.dtos;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RefundPaymentResponse implements PaymentResponse{
    private final String message;

    public RefundPaymentResponse(BigDecimal amount) {
        this.message = "You will be refunded " + amount;
    }
}
