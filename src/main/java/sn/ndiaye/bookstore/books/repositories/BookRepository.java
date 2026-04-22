package sn.ndiaye.bookstore.books.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import sn.ndiaye.bookstore.books.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = {"publisher", "genres"})
    @Query("SELECT b FROM Book b")
    List<Book> getAllBooks();

    @EntityGraph(attributePaths = {"publisher", "genres"})
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> getBook(Long id);

    @Override
    @EntityGraph(attributePaths = {"publisher", "genres"})
    List<Book> findAll(Specification<Book> spec, Sort sort);
}
