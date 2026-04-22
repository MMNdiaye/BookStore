package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.dtos.BookDto;
import sn.ndiaye.bookstore.books.dtos.RegisterBookRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateBookRequest;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.books.entities.Publisher;
import sn.ndiaye.bookstore.books.exceptions.BookAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.BookNotFoundException;
import sn.ndiaye.bookstore.books.exceptions.IsbnAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.PublisherNotFoundException;
import sn.ndiaye.bookstore.books.mappers.BookMapper;
import sn.ndiaye.bookstore.books.mappers.PublisherMapper;
import sn.ndiaye.bookstore.books.repositories.BookRepository;
import sn.ndiaye.bookstore.books.repositories.PublisherRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private PublisherService publisherService;
    private BookMapper bookMapper;

    public BookDto createBook(RegisterBookRequest request) {
        var publisher = getPublisher(request.getPublisher());
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

        var publisher = getPublisher(publisherName);
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
            var publisher = getPublisher(request.getPublisher());
            book.setPublisher(publisher);
        }
        bookMapper.update(book, request);
        return bookMapper.toDto(book);
    }

    private Publisher getPublisher(String name) {
        var publisherDto = publisherService.getPublisher(name);
        return publisherService.ToEntity(publisherDto);
    }
}
