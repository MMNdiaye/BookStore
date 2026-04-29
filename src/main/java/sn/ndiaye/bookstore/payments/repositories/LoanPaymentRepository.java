package sn.ndiaye.bookstore.payments.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.payments.entities.LoanPayment;

public interface LoanPaymentRepository extends CrudRepository<LoanPayment, Long> {
}
