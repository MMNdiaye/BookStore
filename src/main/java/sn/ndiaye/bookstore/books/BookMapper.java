package sn.ndiaye.bookstore.books;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(RegisterBookRequest request);

    @Mapping(target = "publisher", expression = "java(book.getPublisher().getName())")
    BookDto toDto(Book book);
}
