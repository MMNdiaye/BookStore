package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.dtos.RegisterGenreRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateGenreRequest;
import sn.ndiaye.bookstore.books.entities.Genre;
import sn.ndiaye.bookstore.books.exceptions.GenreAlreadySavedException;
import sn.ndiaye.bookstore.books.exceptions.GenreNotFoundException;
import sn.ndiaye.bookstore.books.mappers.GenreMapper;
import sn.ndiaye.bookstore.books.repositories.GenreRepository;

@AllArgsConstructor
@Service
public class GenreService {
    private GenreRepository genreRepository;
    private GenreMapper genreMapper;

    public Genre createGenre(RegisterGenreRequest request) {
        if (genreRepository.existsByName(request.getName()))
            throw new GenreAlreadySavedException(request.getName());

        var genre = genreMapper.toEntity(request);
        genreRepository.save(genre);
        return genre;
    }

    public Iterable<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenre(String name) {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNotFoundException(name));
    }

    @Transactional
    public Genre updateGenre(String name, UpdateGenreRequest request) {
        var genre = getGenre(name);
        if (genreRepository.existsByName(request.getName()))
            throw new GenreAlreadySavedException(request.getName());

        genreMapper.update(genre, request);
        return genre;
    }
}
