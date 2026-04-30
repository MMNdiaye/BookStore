package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResponse {
    private String url;
}
