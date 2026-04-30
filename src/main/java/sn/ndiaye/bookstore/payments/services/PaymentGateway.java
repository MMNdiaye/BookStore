package sn.ndiaye.bookstore.payments.services;

import sn.ndiaye.bookstore.payments.dtos.PaymentRequest;
import sn.ndiaye.bookstore.payments.dtos.PaymentResponse;
import sn.ndiaye.bookstore.payments.dtos.WebHookRequest;
import sn.ndiaye.bookstore.payments.dtos.PaymentData;

public interface PaymentGateway {
    PaymentResponse createCheckout(PaymentRequest request);
    PaymentData parseWebhookEvent(WebHookRequest request);
}
