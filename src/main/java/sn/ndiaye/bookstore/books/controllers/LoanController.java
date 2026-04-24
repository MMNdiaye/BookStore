package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.books.dtos.LoanDto;
import sn.ndiaye.bookstore.books.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.books.exceptions.BookNotFoundException;
import sn.ndiaye.bookstore.books.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.books.services.LoanService;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;

@AllArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanController {
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDto> registerLoan(
            @RequestBody @Valid RegisterLoanRequest request) {
        var loanDto = loanService.createLoan(request);
        return ResponseEntity.ok(loanDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleNoAuthenticatedUser() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({BookNotFoundException.class, DuplicateBookLoanException.class})
    public ResponseEntity<ErrorDto> handleBadBookRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(exception.getMessage()));
    }
}
