package sn.ndiaye.bookstore.books;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
}
