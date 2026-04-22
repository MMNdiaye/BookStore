package sn.ndiaye.bookstore.books.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.books.entities.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    boolean existsByName(String name);
    Optional<Genre> findByName(String name);
}
