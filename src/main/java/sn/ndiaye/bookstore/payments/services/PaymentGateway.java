package sn.ndiaye.bookstore.payments.services;

import sn.ndiaye.bookstore.payments.dtos.*;
import sn.ndiaye.bookstore.payments.entities.PaymentProvider;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentResponse createCheckout(PaymentRequest request);
    PaymentData parseWebhookEvent(WebHookRequest request);
    RefundData createRefund(String externalPaymentId, BigDecimal amount);
    PaymentProvider getProvider();
}
