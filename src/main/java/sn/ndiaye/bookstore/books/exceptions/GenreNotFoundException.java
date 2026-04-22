package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;

@Getter
public class GenreNotFoundException extends RuntimeException {
    private final String name;

    public GenreNotFoundException(String name) {
        super("Genre is not found");
        this.name = name;
    }
}
