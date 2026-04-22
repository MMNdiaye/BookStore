package sn.ndiaye.bookstore.books;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private PublisherRepository publisherRepository;
    private BookMapper bookMapper;

    public BookDto createBook(RegisterBookRequest request) {
        var publisher = publisherRepository.findByName(request.getPublisher())
                .orElseThrow(() -> new PublisherNotFoundException(request.getPublisher()));

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

    public Iterable<BookDto> getAllBooks(String title, String author, String publisherName, String sortBy) {
        List<String> validSort = List.of("title", "author", "publisher");
        if (sortBy == null || !validSort.contains(sortBy))
            sortBy = "title";
        if (sortBy.equals("publisher"))
            sortBy += ".name";

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        var publisher = publisherRepository.findByName(publisherName).orElse(null);
        var book = Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .build();

        Example<Book> example = Example.of(book, matcher);
        var sort = Sort.by(sortBy);
        var books = bookRepository.findAll(example, sort);

        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto getBook(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    @Transactional
    public BookDto update(Long id, UpdateBookRequest request) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (request.getPublisher() != null) {
            var publisher = publisherRepository.findByName(request.getPublisher())
                            .orElseThrow(() -> new PublisherNotFoundException(request.getPublisher()));
            book.setPublisher(publisher);
        }
        bookMapper.update(book, request);
        return bookMapper.toDto(book);
    }
}
