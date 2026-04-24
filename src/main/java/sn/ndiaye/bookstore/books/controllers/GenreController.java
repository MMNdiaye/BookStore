package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.GenreDto;
import sn.ndiaye.bookstore.books.dtos.RegisterGenreRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateGenreRequest;
import sn.ndiaye.bookstore.books.exceptions.GenreAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.GenreNotFoundException;
import sn.ndiaye.bookstore.books.mappers.GenreMapper;
import sn.ndiaye.bookstore.books.services.GenreService;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private GenreService genreService;
    private GenreMapper genreMapper;

    @PostMapping
    public ResponseEntity<GenreDto> registerGenre(
            @RequestBody @Valid RegisterGenreRequest request,
            UriComponentsBuilder uriBuilder
    ){
        var genre = genreService.createGenre(request);
        var uri = uriBuilder.path("/genres/{name}")
                .buildAndExpand(genre.getName()).toUri();
        return ResponseEntity.created(uri)
                .body(genreMapper.toDto(genre));
    }

    @GetMapping
    public Iterable<GenreDto> getAllGenres() {
        var genres = genreService.getAllGenres();
        return genreMapper.toDtos(genres);
    }

    @GetMapping("/{genreName}")
    public GenreDto getGenre(
            @PathVariable("genreName") String name
    ) {
        var genre = genreService.getGenre(name);
        return genreMapper.toDto(genre);
    }

    @PatchMapping("/{genreName}")
    public GenreDto updateGenre(
            @PathVariable("genreName") String name,
            @RequestBody @Valid UpdateGenreRequest request
    ) {
        var genre = genreService.updateGenre(name, request);
        return genreMapper.toDto(genre);
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
