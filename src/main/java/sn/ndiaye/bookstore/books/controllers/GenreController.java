package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.GenreDto;
import sn.ndiaye.bookstore.books.dtos.RegisterGenreRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateGenreRequest;
import sn.ndiaye.bookstore.books.exceptions.GenreAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.GenreNotFoundException;
import sn.ndiaye.bookstore.books.services.GenreService;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDto> registerGenre(
            @RequestBody @Valid RegisterGenreRequest request,
            UriComponentsBuilder uriBuilder
    ){
        var genreDto = genreService.createGenre(request);
        var uri = uriBuilder.path("/genres/{name}")
                .buildAndExpand(genreDto.getName()).toUri();
        return ResponseEntity.created(uri).body(genreDto);
    }

    @GetMapping
    public Iterable<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{genreName}")
    public GenreDto getGenre(
            @PathVariable("genreName") String name
    ) {
        return genreService.getGenre(name);
    }

    @PatchMapping("/{genreName}")
    public GenreDto updateGenre(
            @PathVariable("genreName") String name,
            @RequestBody @Valid UpdateGenreRequest request
    ) {
        return genreService.updateGenre(name, request);
    }

    @ExceptionHandler(GenreAlreadySavedException.class)
    public ResponseEntity<ErrorDto> handleGenreAlreadySaved(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<Void> handleGenreNotFound() {
        return ResponseEntity.notFound().build();
    }
}
