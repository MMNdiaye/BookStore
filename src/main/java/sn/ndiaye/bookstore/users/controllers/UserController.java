package sn.ndiaye.bookstore.users.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.users.exceptions.EmailAlreadyTakenException;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.dtos.UserDto;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.services.UserService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        var userDto = userService.createUser(request);
        var uri = uriComponentsBuilder
                .path("/users/{userId}")
                .buildAndExpand(userDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers() {
        var usersDto = userService.getAllUsers();
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") UUID id) {
        var userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyTaken(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
