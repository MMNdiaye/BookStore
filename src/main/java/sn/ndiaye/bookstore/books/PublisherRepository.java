package sn.ndiaye.bookstore.books;

import org.springframework.data.repository.CrudRepository;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    boolean existsByName(String name);
}
