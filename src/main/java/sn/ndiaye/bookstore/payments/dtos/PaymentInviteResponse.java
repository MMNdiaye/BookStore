package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInviteResponse implements PaymentResponse{
    private String url;
}
