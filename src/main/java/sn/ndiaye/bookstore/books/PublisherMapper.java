package sn.ndiaye.bookstore.books;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    PublisherDto toDto(Publisher publisher);
    Publisher toEntity(RegisterPublisherRequest request);
}
