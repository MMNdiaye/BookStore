package sn.ndiaye.bookstore.payments.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.payments.dtos.*;
import sn.ndiaye.bookstore.payments.entities.LoanPayment;
import sn.ndiaye.bookstore.payments.entities.LoanPaymentReason;
import sn.ndiaye.bookstore.payments.entities.PaymentType;
import sn.ndiaye.bookstore.payments.repositories.LoanPaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class PaymentService {
    private final LoanPaymentRepository loanPaymentRepository;
    private final LoanService loanService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentResponse createLoanCheckout(Loan loan) {
        var paymentRequest = PaymentRequest.initialLoanPayment(loan);
        return paymentGateway.createCheckout(paymentRequest);
    }


    @Transactional
    public PaymentResponse createLoanReturnCheckout(Loan loan) {
        var toPay = loan.processReturnFee();

        if (toPay.compareTo(BigDecimal.ZERO) < 0) {
            var paymentRequest = PaymentRequest.returnLoanPenality(loan);
            return paymentGateway.createCheckout(paymentRequest);
        }

        if (toPay.compareTo(BigDecimal.ZERO) > 0) {
            var loanPayment = loanPaymentRepository
                    .findByLoanAndReason(loan, LoanPaymentReason.INITIAL)
                    .orElse(null);
            if (loanPayment != null) {
                var refundData = paymentGateway
                        .createRefund(loanPayment.getExternalPaymentId(), toPay);
                registerLoanRefund(loan, refundData);
                var paymentResponse =
                        new PaymentConfirmResponse("You will be refunded" + toPay + " from early return");
                loanService.endLoan(loan.getId().toString());
                return paymentResponse;
            }
        }


        // loan is ended on time(no payment) or couldn't find payment to refund
        return new PaymentConfirmResponse("Returned book with success");
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
                    var loan = loanService.confirmLoan(data.getOperationId());
                    registerLoanPayment(loan, LoanPaymentReason.INITIAL, data);
                } else
                    loanService.cancelLoan(data.getOperationId());

            }

            case PENALITY -> {
                if (data.isSuccess()) {
                    var loan = loanService.endLoan(data.getOperationId());
                    registerLoanPayment(loan, LoanPaymentReason.PENALITY, data);
                }

            }

            case ORDER -> {
            }
        }
    }

    private void registerLoanPayment(Loan loan, LoanPaymentReason reason,
                                     PaymentData paymentData) {
        var loanPayment = LoanPayment.builder()
                .loan(loan)
                .createdAt(LocalDateTime.now())
                .reason(reason)
                .cost(paymentData.getCost())
                .provider(paymentData.getProvider())
                .externalPaymentId(paymentData.getExternalPaymentId())
                .build();
        loanPaymentRepository.save(loanPayment);
    }

    private void registerLoanRefund(Loan loan, RefundData refundData) {
        var loanPayment = LoanPayment.builder()
                .loan(loan)
                .createdAt(LocalDateTime.now())
                .reason(LoanPaymentReason.EARLY_RETURN)
                .cost(refundData.getAmount())
                .provider(refundData.getProvider())
                .externalPaymentId(refundData.getRefundId())
                .build();
        loanPaymentRepository.save(loanPayment);
    }

}
