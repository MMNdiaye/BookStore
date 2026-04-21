package sn.ndiaye.bookstore.books;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
