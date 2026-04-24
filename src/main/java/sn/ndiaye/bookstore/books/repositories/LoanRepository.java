package sn.ndiaye.bookstore.books.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.books.entities.Loan;

import java.util.UUID;

public interface LoanRepository extends CrudRepository<Loan, UUID> {
}
