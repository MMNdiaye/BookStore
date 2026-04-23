package sn.ndiaye.bookstore.books.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.books.dtos.PublisherDto;
import sn.ndiaye.bookstore.books.dtos.UpdatePublisherRequest;
import sn.ndiaye.bookstore.books.exceptions.PublisherNotFoundException;
import sn.ndiaye.bookstore.books.services.PublisherService;
import sn.ndiaye.bookstore.books.dtos.RegisterPublisherRequest;
import sn.ndiaye.bookstore.books.exceptions.PublisherAlreadySavedException;
import sn.ndiaye.bookstore.commons.ErrorDto;

@AllArgsConstructor
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherDto> registerPublisher(
            @RequestBody @Valid RegisterPublisherRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        PublisherDto publisherDto = publisherService.createPublisher(request);
        var uri = uriBuilder.path("/publishers/{publisherName}")
                .buildAndExpand(publisherDto.getName()).toUri();
        return ResponseEntity.created(uri).body(publisherDto);
    }

    @GetMapping("/{publisherName}")
    public ResponseEntity<PublisherDto> getPublisher(
            @PathVariable("publisherName") String name
    ) {
        var publisherDto = publisherService.getPublisher(name);
        return ResponseEntity.ok(publisherDto);
    }

    @GetMapping
    public ResponseEntity<Iterable<PublisherDto>> getPublishers() {
        var publisherDtos = publisherService.getAllPublisher();
        return ResponseEntity.ok(publisherDtos);
    }

    @PatchMapping("/{publisherName}")
    public ResponseEntity<PublisherDto> updatePublisher(
            @PathVariable("publisherName") String name,
            @RequestBody @Valid UpdatePublisherRequest request) {
        var publisherDto = publisherService.updatePublisher(name, request);
        return ResponseEntity.ok(publisherDto);
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
