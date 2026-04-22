package sn.ndiaye.bookstore.books.repositories;

import org.springframework.data.repository.CrudRepository;
import sn.ndiaye.bookstore.books.entities.Publisher;

import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    boolean existsByName(String name);
    Optional<Publisher> findByName(String name);
}
