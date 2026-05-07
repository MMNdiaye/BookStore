package sn.ndiaye.bookstore.loans.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.exceptions.EmptyBookStockException;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.loans.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.loans.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.loans.mappers.LoanMapper;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.payments.dtos.PaymentResponse;
import sn.ndiaye.bookstore.payments.exceptions.PaymentException;
import sn.ndiaye.bookstore.payments.services.PaymentService;
import sn.ndiaye.bookstore.users.dtos.UserLoanDto;
import sn.ndiaye.bookstore.users.services.UserService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users/me/loans")
public class UserLoanController {
    private final LoanService loanService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final LoanMapper loanMapper;

    @PostMapping
    public PaymentResponse registerLoan(
            @RequestBody @Valid RegisterLoanRequest request,
            UriComponentsBuilder uriBuilder) {
        var loan = loanService.createLoan(request);
        return paymentService.createInitialLoanCheckout(loan);
    }

    @GetMapping
    public Iterable<UserLoanDto> getAllLoans() {
        var loans = userService.getLoans();
        return loanMapper.toUserLoanDtos(loans);
    }

    @GetMapping("/{loanId}")
    public UserLoanDto getLoan(@PathVariable UUID loanId) {
        var loan = userService.getLoan(loanId);
        return loanMapper.toUserLoanDto(loan);
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<PaymentResponse> endsUserLoan(@PathVariable UUID loanId) {
        var loan = userService.getLoan(loanId);
        var paymentResponse = paymentService.createLoanReturnCheckout(loan);

        if (paymentResponse == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(paymentResponse);
    }

    @ExceptionHandler({DuplicateBookLoanException.class, EmptyBookStockException.class})
    public ResponseEntity<ErrorDto> handleInvalidLoan(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Void> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
