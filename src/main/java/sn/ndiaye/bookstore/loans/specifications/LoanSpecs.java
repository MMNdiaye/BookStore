package sn.ndiaye.bookstore.loans.specifications;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import sn.ndiaye.bookstore.loans.entities.Loan;

import java.util.UUID;

public class LoanSpecs {
    public static Specification<Loan> hasBookId(Long bookId) {
        return (root, query, criteriaBuilder) -> {
            var book = root.join("book", JoinType.LEFT);
            return criteriaBuilder.equal(book.get("id"), bookId);
        };
    }

    public static Specification<Loan> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            var user = root.join("user", JoinType.LEFT);
            return criteriaBuilder.equal(user.get("id"), userId);
        };
    }
}
