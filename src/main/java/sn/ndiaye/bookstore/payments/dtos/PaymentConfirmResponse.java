package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentConfirmResponse implements PaymentResponse{
    private String message;
}
