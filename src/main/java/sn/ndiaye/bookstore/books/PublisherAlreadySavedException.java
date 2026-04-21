package sn.ndiaye.bookstore.books;

import lombok.Getter;

@Getter
public class PublisherAlreadySavedException extends RuntimeException {
    private final String publisherName;

    public PublisherAlreadySavedException(String publisherName) {
        super("This book is already saved");
        this.publisherName = publisherName;
    }
}
