package sn.ndiaye.bookstore.books;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
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
        var publisherDto = publisherService.createPublisher(request);
        var uri = uriBuilder.path("/publishers/{publisherId}")
                .buildAndExpand(publisherDto.getId()).toUri();
        return ResponseEntity.created(uri).body(publisherDto);
    }

    @ExceptionHandler(PublisherAlreadySavedException.class)
    public ResponseEntity<ErrorDto> handleAlreadySavedException(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(exception.getMessage()));
    }
}
