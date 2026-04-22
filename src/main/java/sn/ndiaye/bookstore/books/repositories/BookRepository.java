package sn.ndiaye.bookstore.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ndiaye.bookstore.books.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
