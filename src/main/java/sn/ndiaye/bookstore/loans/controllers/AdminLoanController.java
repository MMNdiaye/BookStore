package sn.ndiaye.bookstore.loans.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.loans.dtos.LoanDto;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.loans.mappers.LoanMapper;
import sn.ndiaye.bookstore.loans.services.LoanService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/loans")
public class AdminLoanController {
    private LoanService loanService;
    private LoanMapper loanMapper;

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

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Void> handleLoanNotFound() {
        return ResponseEntity.notFound().build();
    }
}
