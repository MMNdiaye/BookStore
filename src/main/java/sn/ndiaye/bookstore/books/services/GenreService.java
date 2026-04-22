package sn.ndiaye.bookstore.books.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ndiaye.bookstore.books.dtos.GenreDto;
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

    public GenreDto createGenre(RegisterGenreRequest request) {
        if (genreRepository.existsByName(request.getName()))
            throw new GenreAlreadySavedException(request.getName());

        var genre = genreMapper.toEntity(request);
        genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    public Iterable<GenreDto> getAllGenres() {
        var genres = genreRepository.findAll();
        return genreMapper.toDtos(genres);
    }

    public GenreDto getGenre(String name) {
        var genre = genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNotFoundException(name));
        return genreMapper.toDto(genre);
    }

    @Transactional
    public GenreDto updateGenre(String name,  UpdateGenreRequest request) {
        var genre = genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNotFoundException(name));

        if (genreRepository.existsByName(request.getName()))
            throw new GenreAlreadySavedException(request.getName());

        genreMapper.update(genre, request);
        return genreMapper.toDto(genre);
    }

    public Genre toEntity(GenreDto genreDto) {
        return genreMapper.toEntity(genreDto);
    }
}
