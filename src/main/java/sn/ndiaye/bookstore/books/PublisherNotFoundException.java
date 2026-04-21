package sn.ndiaye.bookstore.books;

import lombok.Getter;

@Getter
public class PublisherNotFoundException extends RuntimeException {
    private final Long id;

    public PublisherNotFoundException(Long id) {
        super("The publisher was not found");
        this.id = id;
    }
}
