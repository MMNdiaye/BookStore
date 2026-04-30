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
import sn.ndiaye.bookstore.payments.dtos.PaymentRequest;
import sn.ndiaye.bookstore.payments.dtos.PaymentResponse;
import sn.ndiaye.bookstore.payments.dtos.WebHookRequest;
import sn.ndiaye.bookstore.payments.entities.LoanPayment;
import sn.ndiaye.bookstore.payments.entities.LoanPaymentReason;
import sn.ndiaye.bookstore.payments.entities.PaymentType;
import sn.ndiaye.bookstore.payments.repositories.LoanPaymentRepository;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class PaymentService {
    private final LoanPaymentRepository loanPaymentRepository;
    private final LoanService loanService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentResponse createLoanCheckout(Loan loan) {
        var paymentRequest = PaymentRequest.builder()
                .book(loan.getBook())
                .paymentType(PaymentType.LOAN)
                .operation_id(loan.getId().toString())
                .quantity(1L)
                .unitCost(loan.processInitialFee())
                .build();
        return paymentGateway.createCheckout(paymentRequest);
    }

    @Transactional
    public void handleWebHookEvent(WebHookRequest webHookRequest) {
        var data = paymentGateway.parseWebhookEvent(webHookRequest);
        if (data == null)
            return;
        var paymentType = PaymentType.valueOf(data.getPaymentType());
        switch (paymentType) {
            case LOAN -> {
                if (data.isSuccess()) {
                    var loan = loanService.confirmLoan(data.getOperation_id());
                    var loanPayment = LoanPayment.builder()
                            .loan(loan)
                            .createdAt(LocalDateTime.now())
                            .reason(LoanPaymentReason.INITIAL)
                            .cost(data.getCost())
                            .build();
                    loanPaymentRepository.save(loanPayment);
                }

                else
                    loanService.cancelLoan(data.getOperation_id());

            }

            case ORDER -> {}
        }
    }



}
