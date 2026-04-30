package sn.ndiaye.bookstore.users.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.auth.exceptions.UnauthenticatedUserException;
import sn.ndiaye.bookstore.auth.services.AuthService;
import sn.ndiaye.bookstore.books.exceptions.EmptyBookStockException;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.loans.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.loans.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.loans.mappers.LoanMapper;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.payments.dtos.PaymentResponse;
import sn.ndiaye.bookstore.payments.exceptions.PaymentException;
import sn.ndiaye.bookstore.payments.services.PaymentService;
import sn.ndiaye.bookstore.users.dtos.UserLoanDto;
import sn.ndiaye.bookstore.users.exceptions.EmailAlreadyTakenException;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.dtos.UserDto;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.mappers.UserMapper;
import sn.ndiaye.bookstore.users.services.UserService;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final LoanService loanService;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final LoanMapper loanMapper;
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.createUser(request);
        var uri = uriComponentsBuilder
                .path("/users/{userId}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(userMapper.toDto(user));
    }

    @GetMapping
    public Iterable<UserDto> getAllUsers() {
        var users = userService.getAllUsers();
        return userMapper.toDtos(users);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") UUID id) {
        var user = userService.getUser(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/me")
    public UserDto getAuthUser() {
        var user = authService.getCurrentUser();
        return userMapper.toDto(user);
    }

    @PostMapping("/me/loans")
    public PaymentResponse registerLoan(
            @RequestBody @Valid RegisterLoanRequest request,
            UriComponentsBuilder uriBuilder) {
        var loan = loanService.createLoan(request);
        return paymentService.createLoanCheckout(loan);
    }

    @GetMapping("/me/loans")
    public Iterable<UserLoanDto> getUserLoans() {
        var loans = userService.getLoans();
        return loanMapper.toUserLoanDtos(loans);
    }

    @GetMapping("/me/loans/{loanId}")
    public UserLoanDto getUserLoan(@PathVariable UUID loanId) {
        var loan = userService.getLoan(loanId);
        return loanMapper.toUserLoanDto(loan);
    }

    @DeleteMapping("/me/loans/{loanId}")
    public BigDecimal endsUserLoan(@PathVariable UUID loanId) {
        return userService.endsLoan(loanId);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyTaken(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({DuplicateBookLoanException.class, EmptyBookStockException.class})
    public ResponseEntity<ErrorDto> handleInvalidLoan(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class, LoanNotFoundException.class})
    public ResponseEntity<Void> handleResourceNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<Void> handeUnauthenticatedUser() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Void> handlePaymentException() {
        return ResponseEntity.internalServerError().build();
    }
}
