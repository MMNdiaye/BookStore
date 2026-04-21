package sn.ndiaye.bookstore.books;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException{
    private final Long id;

    public BookNotFoundException(Long id) {
        this.id = id;
    }
}
