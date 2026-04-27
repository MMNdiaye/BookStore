package sn.ndiaye.bookstore.loans.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.auth.exceptions.UnauthenticatedUserException;
import sn.ndiaye.bookstore.books.exceptions.BookNotFoundException;
import sn.ndiaye.bookstore.books.exceptions.EmptyBookStockException;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.loans.dtos.LoanDto;
import sn.ndiaye.bookstore.loans.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.loans.dtos.UpdateLoanRequest;
import sn.ndiaye.bookstore.loans.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.loans.mappers.LoanMapper;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanController {
    private LoanService loanService;
    private LoanMapper loanMapper;

    @PostMapping
    public ResponseEntity<LoanDto> registerLoan(
            @RequestBody @Valid RegisterLoanRequest request,
            UriComponentsBuilder uriBuilder) {
        var loan = loanService.createLoan(request);
        var uri = uriBuilder.path("/loans/{loanId}")
                .buildAndExpand(loan.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(loanMapper.toDto(loan));
    }

    @GetMapping
    public Iterable<LoanDto> getAllLoans(
            @RequestParam(name = "book-id", required = false) Long bookId,
            @RequestParam(name = "user-id", required = false) UUID userId,
            @RequestParam(name = "due", required = false) Boolean isDue
    ) {
        var loans = loanService.getAllLoans(bookId, userId, isDue);
        return loanMapper.toDtos(loans);
    }

    @GetMapping("/{loanId}")
    public LoanDto getLoan(@PathVariable UUID loanId) {
        var loan = loanService.getLoan(loanId);
        return loanMapper.toDto(loan);
    }

    @PatchMapping("/{loanId}")
    public LoanDto updateLoan(
            @PathVariable UUID loanId,
            @RequestBody @Valid UpdateLoanRequest request) {
        var loan = loanService.updateLoan(loanId, request);
        return loanMapper.toDto(loan);
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<Void> handleNoAuthenticatedUser() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({BookNotFoundException.class, DuplicateBookLoanException.class,
                        EmptyBookStockException.class})
    public ResponseEntity<ErrorDto> handleBadBookRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Void> handleLoanNotFound() {
        return ResponseEntity.notFound().build();
    }
}
