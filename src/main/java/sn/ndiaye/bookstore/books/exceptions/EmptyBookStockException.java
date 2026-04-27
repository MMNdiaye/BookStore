package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;
import sn.ndiaye.bookstore.books.entities.Book;

@Getter
public class EmptyBookStockException extends RuntimeException {
    private final Book book;

    public EmptyBookStockException(Book book) {
        super("This book has no stock remaining");
        this.book = book;
    }
}
