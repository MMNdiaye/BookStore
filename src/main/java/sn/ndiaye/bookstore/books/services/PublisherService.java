package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.dtos.PublisherDto;
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

    public PublisherDto createPublisher(RegisterPublisherRequest request) {
        if (publisherRepository.existsByName(request.getName()))
            throw new PublisherAlreadySavedException(request.getName());

        var publisher = publisherMapper.toEntity(request);
        publisherRepository.save(publisher);
        return publisherMapper.toDto(publisher);
    }

    public PublisherDto getPublisher(String name) {
        var publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new PublisherNotFoundException(name));

        return publisherMapper.toDto(publisher);
    }

    public Iterable<PublisherDto> getAllPublisher() {
        var publishers = publisherRepository.findAll();
        return publisherMapper.dtosOf(publishers);
    }

    @Transactional
    public PublisherDto updatePublisher(String name, UpdatePublisherRequest request) {
        if (publisherRepository.existsByName(request.getName()))
            throw new PublisherAlreadySavedException(request.getName());

        var publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new PublisherNotFoundException(name));

        publisherMapper.update(publisher, request);
        return publisherMapper.toDto(publisher);
    }

    public Publisher ToEntity(PublisherDto publisherDto) {
        return publisherMapper.toEntity(publisherDto);
    }
}
