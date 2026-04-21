package sn.ndiaye.bookstore.books;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Iterable<BookDto>> getAllBooks() {
        return null;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBook() {
        return null;
    }

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

    @ExceptionHandler({BookAlreadySavedException.class,
            PublisherNotFoundException.class, IsbnAlreadySavedException.class})
    public ResponseEntity<ErrorDto> handleInvalidBookRequest(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }
}
