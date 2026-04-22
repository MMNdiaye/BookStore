package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;

@Getter
public class GenreAlreadySavedException extends RuntimeException {
    private final String name;

    public GenreAlreadySavedException(String name) {
        super("This genre is already saved");
        this.name = name;
    }
}
