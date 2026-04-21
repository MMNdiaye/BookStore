package sn.ndiaye.bookstore.books;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    boolean existsByName(String name);
    Optional<Publisher> findByName(String name);
}
