package sn.ndiaye.bookstore.books.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sn.ndiaye.bookstore.books.dtos.PublisherDto;
import sn.ndiaye.bookstore.books.dtos.RegisterPublisherRequest;
import sn.ndiaye.bookstore.books.dtos.UpdatePublisherRequest;
import sn.ndiaye.bookstore.books.entities.Publisher;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    PublisherDto toDto(Publisher publisher);
    Publisher toEntity(PublisherDto publisherDto);
    Publisher toEntity(RegisterPublisherRequest request);
    Iterable<PublisherDto> toDtos(Iterable<Publisher> publishers);
    void update(@MappingTarget Publisher publisher, UpdatePublisherRequest request);
}
