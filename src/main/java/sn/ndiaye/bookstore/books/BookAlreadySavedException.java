package sn.ndiaye.bookstore.books;

import lombok.Getter;

@Getter
public class BookAlreadySavedException extends RuntimeException {
    private final Book book;

    public BookAlreadySavedException(Book book) {
        super("This book is already saved");
        this.book = book;
    }
}
