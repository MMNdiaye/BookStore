package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;
import sn.ndiaye.bookstore.books.entities.Book;

@Getter
public class BookAlreadySavedException extends RuntimeException {
    private final Book book;

    public BookAlreadySavedException(Book book) {
        super("This book is already saved");
        this.book = book;
    }
}
