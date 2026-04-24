package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.PublisherDto;
import sn.ndiaye.bookstore.books.dtos.UpdatePublisherRequest;
import sn.ndiaye.bookstore.books.exceptions.PublisherNotFoundException;
import sn.ndiaye.bookstore.books.mappers.PublisherMapper;
import sn.ndiaye.bookstore.books.services.PublisherService;
import sn.ndiaye.bookstore.books.dtos.RegisterPublisherRequest;
import sn.ndiaye.bookstore.books.exceptions.PublisherAlreadySavedException;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private PublisherService publisherService;
    private PublisherMapper publisherMapper;

    @PostMapping
    public ResponseEntity<PublisherDto> registerPublisher(
            @RequestBody @Valid RegisterPublisherRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var publisher = publisherService.createPublisher(request);
        var uri = uriBuilder.path("/publishers/{publisherName}")
                .buildAndExpand(publisher.getName()).toUri();
        return ResponseEntity.created(uri)
                .body(publisherMapper.toDto(publisher));
    }

    @GetMapping("/{publisherName}")
    public PublisherDto getPublisher(
            @PathVariable("publisherName") String name
    ) {
        var publisher = publisherService.getPublisher(name);
        return publisherMapper.toDto(publisher);
    }

    @GetMapping
    public Iterable<PublisherDto> getPublishers() {
        var publishers = publisherService.getAllPublishers();
        return publisherMapper.toDtos(publishers);
    }

    @PatchMapping("/{publisherName}")
    public PublisherDto updatePublisher(
            @PathVariable("publisherName") String name,
            @RequestBody @Valid UpdatePublisherRequest request) {
        var publisher = publisherService.updatePublisher(name, request);
        return publisherMapper.toDto(publisher);
    }

    @ExceptionHandler(PublisherAlreadySavedException.class)
    public ResponseEntity<ErrorDto> handleAlreadySavedException(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(PublisherNotFoundException.class)
    public ResponseEntity<ErrorDto> handlePublisherNotFound() {
        return ResponseEntity.notFound().build();
    }
}
