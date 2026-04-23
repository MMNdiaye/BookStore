package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.books.dtos.AddGenresToBookRequest;
import sn.ndiaye.bookstore.books.services.BookService;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.BookDto;
import sn.ndiaye.bookstore.books.dtos.RegisterBookRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateBookRequest;
import sn.ndiaye.bookstore.books.exceptions.BookAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.BookNotFoundException;
import sn.ndiaye.bookstore.books.exceptions.IsbnAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.PublisherNotFoundException;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> registerBook(
            @RequestBody @Valid RegisterBookRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var bookDto = bookService.createBook(request);
        var uri = uriBuilder.path("/books/{bookId}")
                .buildAndExpand(bookDto.getId()).toUri();

        return ResponseEntity.created(uri).body(bookDto);
    }

    @GetMapping
    public ResponseEntity<Iterable<BookDto>> getAllBooks(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "sort", required = false) String sortBy
    ) {
        var books = bookService.getAllBooks(title, author, publisher, sortBy);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBook(@PathVariable("bookId") Long id) {
        var bookDto = bookService.getBook(id);
        return ResponseEntity.ok(bookDto);
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable("bookId") Long id,
            @RequestBody UpdateBookRequest request) {
        var bookDto = bookService.update(id, request);
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("/{bookId}/genres")
    public ResponseEntity<BookDto> addGenres(
            @PathVariable("bookId") Long id,
            @RequestBody @Valid AddGenresToBookRequest request) {
        var bookDto = bookService.addGenresToBook(request, id);
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/{bookId}/genres/{genreName}")
    public ResponseEntity<BookDto> removeGenre(
            @PathVariable("bookId") Long id,
            @PathVariable("genreName") String genre
    ) {
        var bookDto = bookService.removeGenreFromBook(genre, id);
        return ResponseEntity.ok(bookDto);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Void> handleBookNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({BookAlreadySavedException.class,
            PublisherNotFoundException.class, IsbnAlreadySavedException.class})
    public ResponseEntity<ErrorDto> handleInvalidBookRequest(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }
}
