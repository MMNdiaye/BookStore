package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.BookSpecs;
import sn.ndiaye.bookstore.books.dtos.*;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.books.entities.Genre;
import sn.ndiaye.bookstore.books.entities.Publisher;
import sn.ndiaye.bookstore.books.exceptions.*;
import sn.ndiaye.bookstore.books.mappers.BookMapper;
import sn.ndiaye.bookstore.books.repositories.BookRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private PublisherService publisherService;
    private GenreService genreService;
    private BookMapper bookMapper;

    @Transactional
    public BookDto createBook(RegisterBookRequest request) {
        var book = bookMapper.toEntity(request);
        var publisher = publisherService.findPublisherEntity(request.getPublisher());

        for (var genreName : request.getGenres()) {
            var genre = getGenreOrCreate(genreName);
            if (book.hasGenre(genre))
                throw new DuplicatedGenreException(genreName);

            book.addGenre(genre);
        }
        book.setPublisher(publisher);

        if (bookRepository.existsByIsbn(book.getIsbn()))
            throw new IsbnAlreadySavedException(book.getIsbn());

        if(existsBook(book))
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

        Specification<Book> spec = Specification.unrestricted();
        if (title != null)
            spec = spec.and(BookSpecs.hasTitle(title));
        if (author != null)
            spec = spec.and(BookSpecs.hasAuthor(author));
        if (publisherName != null)
            spec = spec.and(BookSpecs.hasPublisher(publisherName));
        var sort = Sort.by(sortBy);
        var books = bookRepository.findAll(spec, sort);

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

        if (bookRepository.existsByIsbn(request.getIsbn()))
            throw new IsbnAlreadySavedException(request.getIsbn());

        if (request.getPublisher() != null) {
            var publisher = publisherService.findPublisherEntity(request.getPublisher());
            book.setPublisher(publisher);
        }

        if (existsBook(book))
            throw new BookAlreadySavedException(book);

        bookMapper.update(book, request);
        return bookMapper.toDto(book);
    }

    @Transactional
    public BookDto addGenresToBook(AddGenresToBookRequest request, Long bookId) {
        var book = bookRepository.getBook(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        for (var genreName : request.getGenres()) {
            var genre = getGenreOrCreate(genreName);
            if (book.hasGenre(genre))
                throw  new DuplicatedGenreException(genreName);

            book.addGenre(genre);
        }
        return bookMapper.toDto(book);
    }

    @Transactional
    public BookDto removeGenreFromBook(String genreName, Long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        book.removeGenre(genreName);
        return bookMapper.toDto(book);
    }

    private Genre getGenreOrCreate(String genreName) {
        try {
            return genreService.findGenreEntity(genreName);
        } catch (GenreNotFoundException e) {
            return genreService.createGenreEntity(new RegisterGenreRequest(genreName));
        }
    }

    private boolean existsBook(Book book) {
        var exampleMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isbn");
        Example<Book> example = Example.of(book, exampleMatcher);
        return bookRepository.exists(example);
    }

}
