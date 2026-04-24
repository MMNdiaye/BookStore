package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.dtos.UpdatePublisherRequest;
import sn.ndiaye.bookstore.books.entities.Publisher;
import sn.ndiaye.bookstore.books.exceptions.PublisherNotFoundException;
import sn.ndiaye.bookstore.books.mappers.PublisherMapper;
import sn.ndiaye.bookstore.books.repositories.PublisherRepository;
import sn.ndiaye.bookstore.books.dtos.RegisterPublisherRequest;
import sn.ndiaye.bookstore.books.exceptions.PublisherAlreadySavedException;

@AllArgsConstructor
@Service
public class PublisherService {
    private PublisherRepository publisherRepository;
    private PublisherMapper publisherMapper;


    public Publisher createPublisher(RegisterPublisherRequest request) {
        if (publisherRepository.existsByName(request.getName()))
            throw new PublisherAlreadySavedException(request.getName());

        var publisher = publisherMapper.toEntity(request);
        publisherRepository.save(publisher);
        return publisher;
    }

    public Publisher getPublisher(String name) {
        return publisherRepository.findByName(name)
                .orElseThrow(() -> new PublisherNotFoundException(name));
    }

    public Iterable<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Transactional
    public Publisher updatePublisher(String name, UpdatePublisherRequest request) {
        if (publisherRepository.existsByName(request.getName()))
            throw new PublisherAlreadySavedException(request.getName());

        var publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new PublisherNotFoundException(name));

        publisherMapper.update(publisher, request);
        return publisher;
    }
}
