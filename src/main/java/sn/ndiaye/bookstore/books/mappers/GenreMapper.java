package sn.ndiaye.bookstore.books.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sn.ndiaye.bookstore.books.dtos.GenreDto;
import sn.ndiaye.bookstore.books.dtos.RegisterGenreRequest;
import sn.ndiaye.bookstore.books.dtos.UpdateGenreRequest;
import sn.ndiaye.bookstore.books.entities.Genre;


@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);
    Genre toEntity(GenreDto genreDto);
    Genre toEntity(RegisterGenreRequest request);
    Iterable<GenreDto> toDtos(Iterable<Genre> genres);
    void update(@MappingTarget Genre genre, UpdateGenreRequest request);
}
