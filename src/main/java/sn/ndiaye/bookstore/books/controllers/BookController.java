package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.books.dtos.AddGenresToBookRequest;
import sn.ndiaye.bookstore.books.exceptions.*;
import sn.ndiaye.bookstore.books.mappers.BookMapper;
import sn.ndiaye.bookstore.books.services.BookService;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.BookDto;
import sn.ndiaye.bookstore.books.dtos.RegisterBookRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateBookRequest;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;
    private BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<BookDto> registerBook(
            @RequestBody @Valid RegisterBookRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var book = bookService.createBook(request);
        var uri = uriBuilder.path("/books/{bookId}")
                .buildAndExpand(book.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(bookMapper.toDto(book));
    }

    @GetMapping
    public Iterable<BookDto> getAllBooks(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "sort", required = false) String sortBy
    ) {
        var books = bookService.getAllBooks(title, author, publisher, sortBy);
        return bookMapper.toDtos(books);
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") Long id) {
        var book = bookService.getBook(id);
        return bookMapper.toDto(book);
    }

    @PatchMapping("/{bookId}")
    public BookDto updateBook(
            @PathVariable("bookId") Long id,
            @RequestBody UpdateBookRequest request) {
        var book = bookService.updateBook(id, request);
        return bookMapper.toDto(book);
    }

    @PostMapping("/{bookId}/genres")
    public ResponseEntity<Void> addGenres(
            @PathVariable("bookId") Long id,
            @RequestBody @Valid AddGenresToBookRequest request) {
        bookService.addGenresToBook(request, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}/genres/{genreName}")
    public ResponseEntity<BookDto> removeGenre(
            @PathVariable("bookId") Long id,
            @PathVariable("genreName") String genre
    ) {
        bookService.removeGenreFromBook(genre, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Void> handleBookNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(
            {BookAlreadySavedException.class,
            PublisherNotFoundException.class,
            IsbnAlreadySavedException.class,
            DuplicatedGenreException.class}
    )
    public ResponseEntity<ErrorDto> handleInvalidBookRequest(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }
}
