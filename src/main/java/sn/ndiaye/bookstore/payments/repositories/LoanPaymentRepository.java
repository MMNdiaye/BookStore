package sn.ndiaye.bookstore.payments.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.payments.entities.LoanPayment;
import sn.ndiaye.bookstore.payments.entities.LoanPaymentReason;

import java.util.Optional;

public interface LoanPaymentRepository extends CrudRepository<LoanPayment, Long> {
    Optional<LoanPayment> findByLoanAndReason(Loan loan, LoanPaymentReason reason);
}
