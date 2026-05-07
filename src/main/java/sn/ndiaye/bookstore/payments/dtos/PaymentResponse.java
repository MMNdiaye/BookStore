package sn.ndiaye.bookstore.payments.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentResponse {
    private String url;
    private String message;

    public PaymentResponse(String url) {
        this.url = url;
    }
}
