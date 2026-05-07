ALTER TABLE loan_payments
    ADD COLUMN provider VARCHAR(255) NOT NULL,
    ADD COLUMN external_payment_id VARCHAR(255) NOT NULL;

CREATE UNIQUE INDEX unique_initial_loan_payment
    ON loan_payments(loan_id, reason)
        WHERE reason IN ('INITIAL', 'PENALITY', 'EARLY_RETURN');

CREATE INDEX ON loan_payments(loan_id, reason);