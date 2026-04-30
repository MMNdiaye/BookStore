package sn.ndiaye.bookstore.payments.services;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.payments.dtos.PaymentRequest;
import sn.ndiaye.bookstore.payments.dtos.PaymentResponse;
import sn.ndiaye.bookstore.payments.dtos.WebHookRequest;
import sn.ndiaye.bookstore.payments.dtos.PaymentData;
import sn.ndiaye.bookstore.payments.exceptions.PaymentException;

import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public PaymentResponse createCheckout(PaymentRequest request) {
        try {
            var session = Session.create(getParams(request));
            return new PaymentResponse(session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException("Couldn't create stripe session");
        }
    }

    @Override
    public PaymentData parseWebhookEvent(WebHookRequest request) {
        var signature = request.getHeaders().get("stripe-signature");
        try {
            var event = Webhook.constructEvent(request.getPayload(), signature, webhookSecretKey);
            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                        getPaymentData(event, true);

                case "payment_intent.payment_failed" ->
                        getPaymentData(event, false);

                default -> null;
            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Incorrect stripe signature");
        }

    }

    private static PaymentData getPaymentData(Event event, boolean hasSucceeded) {
        var stripeObject = event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new PaymentException("Couldn't deserialize stripe object"));
        var paymentIntent = (PaymentIntent) stripeObject;
        var metadata = paymentIntent.getMetadata();
        return PaymentData.builder()
                .operation_id(metadata.get("operation_id"))
                .paymentType(metadata.get("type"))
                .cost(BigDecimal.valueOf(paymentIntent.getAmount()).divide(BigDecimal.valueOf(100)))
                .success(hasSucceeded)
                .build();
    }

    private static SessionCreateParams getParams(PaymentRequest request) {

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://example.com/success")
                .setCancelUrl("http://example.com/cancel")
                .setPaymentIntentData(getPaymentIntentData(request))
                .addLineItem(getLineItem(request))
                .build();
    }

    private static SessionCreateParams.PaymentIntentData getPaymentIntentData(PaymentRequest request) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("operation_id", request.getOperation_id())
                .putMetadata("type", request.getType())
                .build();
    }

    private static SessionCreateParams.LineItem getLineItem(PaymentRequest request) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(getPriceData(request))
                .setQuantity(request.getQuantity())
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData getPriceData(PaymentRequest request) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setProductData(getProductData(request))
                .setCurrency("usd")
                .setUnitAmountDecimal(request.getUnitCost().multiply(BigDecimal.valueOf(100)))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData getProductData(PaymentRequest request) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(request.getBookName())
                .build();
    }
}