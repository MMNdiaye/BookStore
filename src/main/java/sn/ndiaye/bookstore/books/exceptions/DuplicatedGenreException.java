package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;

@Getter
public class DuplicatedGenreException extends RuntimeException {
    private String genreName;

    public DuplicatedGenreException(String genreName) {
        super("Genre " + genreName + " is duplicated");
        this.genreName = genreName;
    }
}
