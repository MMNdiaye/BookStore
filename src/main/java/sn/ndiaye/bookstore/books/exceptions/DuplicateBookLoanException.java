package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;
import sn.ndiaye.bookstore.books.entities.Book;

@Getter
public class DuplicateBookLoanException extends RuntimeException {
    private final Book book;

    public DuplicateBookLoanException(Book book) {
        super("Already loaned this book");
        this.book = book;
    }
}
