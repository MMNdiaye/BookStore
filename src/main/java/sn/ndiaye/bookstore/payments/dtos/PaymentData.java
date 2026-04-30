package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentData {
    private String operation_id;
    private String paymentType;
    private BigDecimal cost;
    private boolean success;
}
