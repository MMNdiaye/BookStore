package sn.ndiaye.bookstore.books;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private PublisherRepository publisherRepository;
    private BookMapper bookMapper;

    public BookDto createBook(RegisterBookRequest request) {
        var publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new PublisherNotFoundException(request.getPublisherId()));

        var book = bookMapper.toEntity(request);
        book.setPublisher(publisher);

        if (bookRepository.existsByIsbn(book.getIsbn()))
            throw new IsbnAlreadySavedException(book.getIsbn());

        var exampleMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isbn");
        Example<Book> example = Example.of(book, exampleMatcher);
        if (bookRepository.exists(example))
            throw new BookAlreadySavedException(book);

        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

}
