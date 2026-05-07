ALTER TABLE loans
DROP CONSTRAINT loans_book_id_user_id_key;

CREATE UNIQUE INDEX unique_started_book_loan
    ON loans(book_id, id)
    WHERE status = 'STARTED';
