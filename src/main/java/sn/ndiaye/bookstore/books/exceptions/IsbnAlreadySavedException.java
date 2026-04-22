package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;

@Getter
public class IsbnAlreadySavedException extends RuntimeException {
    private final String isbn;

    public IsbnAlreadySavedException(String isbn) {
        super("Book with this isbn is already registered");
        this.isbn = isbn;
    }
}
