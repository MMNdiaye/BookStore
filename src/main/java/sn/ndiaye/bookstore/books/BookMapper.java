package sn.ndiaye.bookstore.books;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "publisher", ignore = true)
    Book toEntity(RegisterBookRequest request);

    @Mapping(target = "publisher", expression = "java(book.getPublisher().getName())")
    BookDto toDto(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "publisher", ignore = true)
    void update(@MappingTarget Book book, UpdateBookRequest request);
}
