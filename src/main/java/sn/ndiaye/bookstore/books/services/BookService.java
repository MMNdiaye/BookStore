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

    public Book createBook(RegisterBookRequest request) {
        var book = bookMapper.toEntity(request);
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IsbnAlreadySavedException(book.getIsbn());
        }

        // Books with same properties(author + title + publisher + ...) may be a duplication mistake
        if(existsBook(book))
            throw new BookAlreadySavedException(book);

        var publisher = publisherService.getPublisher(request.getPublisher());
        book.setPublisher(publisher);

        for (var genreName : request.getGenres()) {
            var genre = getGenreOrCreate(genreName);
            if (book.hasGenre(genre)) {
                throw new DuplicatedGenreException(genreName);
            }
            book.addGenre(genre);
        }

        bookRepository.save(book);
        return book;
    }

    public Iterable<Book> getAllBooks(String title, String author, String publisherName, String sortBy) {
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
        return bookRepository.findAll(spec, sort);
    }


    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public Book updateBook(Long id, UpdateBookRequest request) {
        var book = getBook(id);
        if (bookRepository.existsByIsbn(request.getIsbn()))
            throw new IsbnAlreadySavedException(request.getIsbn());

        if (request.getPublisher() != null) {
            var publisher = publisherService.getPublisher(request.getPublisher());
            book.setPublisher(publisher);
        }
        bookMapper.update(book, request);
        return book;
    }

    @Transactional
    public void addGenresToBook(AddGenresToBookRequest request, Long bookId) {
        var book = bookRepository.getBook(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        for (var genreName : request.getGenres()) {
            var genre = getGenreOrCreate(genreName);
            if (book.hasGenre(genre))
                throw  new DuplicatedGenreException(genreName);

            book.addGenre(genre);
        }
    }

    @Transactional
    public void removeGenreFromBook(String genreName, Long bookId) {
        var book = getBook(bookId);
        book.removeGenre(genreName);
    }

    private Genre getGenreOrCreate(String genreName) {
        try {
            return genreService.getGenre(genreName);
        } catch (GenreNotFoundException e) {
            return genreService.createGenre(new RegisterGenreRequest(genreName));
        }
    }

    private boolean existsBook(Book book) {
        var exampleMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isbn", "genres", "quantity");
        Example<Book> example = Example.of(book, exampleMatcher);
        return bookRepository.exists(example);
    }

}
