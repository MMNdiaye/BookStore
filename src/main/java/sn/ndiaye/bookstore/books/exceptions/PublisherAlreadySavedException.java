package sn.ndiaye.bookstore.books.exceptions;

import lombok.Getter;

@Getter
public class PublisherAlreadySavedException extends RuntimeException {
    private final String publisherName;

    public PublisherAlreadySavedException(String publisherName) {
        super("This publisher is already saved");
        this.publisherName = publisherName;
    }
}
