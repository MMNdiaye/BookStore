package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.auth.services.AuthService;
import sn.ndiaye.bookstore.books.dtos.LoanDto;
import sn.ndiaye.bookstore.books.dtos.RegisterLoanRequest;
import sn.ndiaye.bookstore.books.entities.Loan;
import sn.ndiaye.bookstore.books.exceptions.DuplicateBookLoanException;
import sn.ndiaye.bookstore.books.mappers.LoanMapper;
import sn.ndiaye.bookstore.books.repositories.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class LoanService {
    private LoanRepository loanRepository;
    private BookService bookService;
    private AuthService authService;
    private final BigDecimal LOAN_RATE_PER_DAY = BigDecimal.valueOf(1.5);
    private LoanMapper loanMapper;

    public LoanDto createLoan(RegisterLoanRequest request) {
        var loan = createLoanEntity(request);
        return loanMapper.toDto(loan);
    }

    private Loan createLoanEntity(RegisterLoanRequest request) {
        var customer = authService.getCurrentUser();
        var book = bookService.findBookEntity(request.getBookId());
        if (customer.hasLoanedBook(book))
            throw new DuplicateBookLoanException(book);

        var durationInDays = request.getDurationInDays();
        var loan = Loan.builder()
                .user(customer)
                .book(book)
                .durationInDays(durationInDays)
                .takenAt(LocalDateTime.now())
                .ratePerDay(LOAN_RATE_PER_DAY)
                .build();
        loan.setInitialFee(loan.processInitialFee());
        loanRepository.save(loan);
        return loan;
    }
}
