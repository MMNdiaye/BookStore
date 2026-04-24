package sn.ndiaye.bookstore.loans.exceptions;


import lombok.Getter;

import java.util.UUID;

@Getter
public class LoanNotFoundException extends RuntimeException {
    private final UUID id;

    public LoanNotFoundException(UUID id) {
        super("Loan not found");
        this.id = id;
    }
}
