package sn.ndiaye.bookstore.books;

import lombok.Getter;

@Getter
public class PublisherNotFoundException extends RuntimeException {
    private final String name;

    public PublisherNotFoundException(String name) {
        super("The publisher was not found");
        this.name = name;
    }
}
