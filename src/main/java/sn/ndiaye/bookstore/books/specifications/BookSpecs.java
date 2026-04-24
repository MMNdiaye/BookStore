package sn.ndiaye.bookstore.books.specifications;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import sn.ndiaye.bookstore.books.entities.Book;

public class BookSpecs {
    public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) ->
            cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) ->
                cb.like(root.get("author"), "%" + author + "%");
    }

    public static Specification<Book> hasPublisher(String publisherName) {
        return (root, query, cb) -> {
            var book = root.join("publisher", JoinType.LEFT);
            return cb.like(book.get("name"), "%" + publisherName + "%");
        };
    }
}
