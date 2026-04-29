package sn.ndiaye.bookstore.payments.services;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.loans.entities.LoanStatus;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.payments.dtos.WebHookRequest;
import sn.ndiaye.bookstore.payments.entities.LoanPayment;
import sn.ndiaye.bookstore.payments.entities.LoanPaymentReason;
import sn.ndiaye.bookstore.payments.exceptions.PaymentException;
import sn.ndiaye.bookstore.payments.repositories.LoanPaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final LoanPaymentRepository loanPaymentRepository;
    private final LoanService loanService;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Transactional
    public String createLoanCheckout(Loan loan) {
        try {
            var session = Session.create(getParams(loan));
            return session.getUrl();
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException("Couldn't load stripe checkout");
        }
    }

    @Transactional
    public void handleWebHookEvent(WebHookRequest webHookRequest) {
        try {
            var payload = webHookRequest.getPayload();
            var signature = webHookRequest.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            switch (event.getType())  {
                case "payment.intent.succeeded" -> {
                    var loan = extractLoanFrom(event);
                    var loanPayment = LoanPayment.builder()
                            .loan(loan)
                            .createdAt(LocalDateTime.now())
                            .reason(LoanPaymentReason.INITIAL)
                            .cost(loan.processInitialFee())
                            .build();
                    loan.setStatus(LoanStatus.STARTED);
                    loanPaymentRepository.save(loanPayment);
                }

                case "payment.intent.payment.failed" -> {
                    var loan = extractLoanFrom(event);
                    loan.setStatus(LoanStatus.CANCELLED);
                }
            }


        } catch (SignatureVerificationException e) {
            throw new PaymentException("Incorrect signature");
        }
    }

    private Loan extractLoanFrom(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize stripe object"));
        var paymentIntent = (PaymentIntent) stripeObject;
        var loanId = paymentIntent.getMetadata().get("loan_id");
        return loanService.getLoan(UUID.fromString(loanId));
    }

    private static SessionCreateParams getParams(Loan loan) {
        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://example.com/success")
                .setCancelUrl("http://example.com/cancel")
                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("loan_id", loan.getId().toString()).build())
                .addLineItem(getLineItem(loan))
                .build();
    }

    private static SessionCreateParams.LineItem getLineItem(Loan loan) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(getPriceData(loan))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData getPriceData(Loan loan) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(loan.processInitialFee()
                        .multiply(BigDecimal.valueOf(100)))
                .setProductData(getProductData(loan))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData getProductData(Loan loan) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(loan.getBook().getTitle())
                .build();
    }

}
