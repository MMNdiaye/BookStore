package sn.ndiaye.bookstore.loans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sn.ndiaye.bookstore.loans.entities.Loan;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID>, JpaSpecificationExecutor<Loan> {
}
