package sn.ndiaye.bookstore.loans.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.loans.entities.Loan;

import java.util.UUID;

public interface LoanRepository extends CrudRepository<Loan, UUID>, JpaSpecificationExecutor<Loan> {
}
