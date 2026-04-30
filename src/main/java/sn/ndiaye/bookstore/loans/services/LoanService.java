package sn.ndiaye.bookstore.loans.services;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.auth.services.AuthService;
import sn.ndiaye.bookstore.books.exceptions.EmptyBookStockException;
import sn.ndiaye.bookstore.books.services.BookService;
import sn.ndiaye.bookstore.loans.dtos.UpdateLoanRequest;
import sn.ndiaye.bookstore.loans.entities.LoanStatus;
import sn.ndiaye.bookstore.loans.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.loans.mappers.LoanMapper;
import sn.ndiaye.bookstore.loans.repositories.LoanRepository;
import sn.ndiaye.bookstore.loans.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.loans.specifications.LoanSpecs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class LoanService {
    private LoanRepository loanRepository;
    private BookService bookService;
    private AuthService authService;
    private final BigDecimal LOAN_RATE_PER_DAY = BigDecimal.valueOf(1.5);
    private LoanMapper loanMapper;

    @Transactional
    public Loan createLoan(RegisterLoanRequest request) {
        var customer = authService.getCurrentUser();
        var book = bookService.getBook(request.getBookId());

        if (customer.hasLoanedBook(book))
            throw new DuplicateBookLoanException(book);

        if (!book.hasAvailableCopies())
            throw new EmptyBookStockException(book);

        var durationInDays = request.getDurationInDays();
        var loan = Loan.builder()
                .user(customer)
                .book(book)
                .durationInDays(durationInDays)
                .takenAt(LocalDateTime.now())
                .ratePerDay(LOAN_RATE_PER_DAY)
                .status(LoanStatus.CONFIRMING)
                .build();
        loanRepository.save(loan);
        book.reduceQuantity(1L);
        return loan;
    }

    public Iterable<Loan> getAllLoans(Long bookId, UUID userId, Boolean isDue) {
        Specification<Loan> spec = Specification.unrestricted();
        if (bookId != null)
            spec = spec.and(LoanSpecs.hasBookId(bookId));
        if (userId != null)
            spec = spec.and(LoanSpecs.hasUserId(userId));
        var loans = loanRepository.findAll(spec);

        if (isDue != null)
            loans = loans.stream()
                    .filter(loan -> loan.isDue() == isDue)
                    .toList();
        return loans;
    }


    public Loan getLoan(UUID loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
    }

    @Transactional
    public Loan updateLoan(UUID loanId, UpdateLoanRequest request) {
        var loan = getLoan(loanId);
        loanMapper.update(loan, request);
        return loan;
    }

    @Transactional
    public void deleteLoan(UUID loanId) {
        var loan = loanRepository.findById(loanId)
                        .orElse(null);
        if (loan == null)
            return;
        var book = loan.getBook();
        loanRepository.delete(loan);
        book.addQuantity(1L);

    }

    @Transactional
    public Loan confirmLoan(String operationId) {
        var id = UUID.fromString(operationId);
        var loan = getLoan(id);
        loan.setStatus(LoanStatus.STARTED);
        return loan;
    }

    @Transactional
    public void cancelLoan(String operationId) {
        var id = UUID.fromString(operationId);
        var loan = getLoan(id);
        loan.setStatus(LoanStatus.CANCELLED);
        loan.getBook().addQuantity(1L);
    }
}
