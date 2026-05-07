package sn.ndiaye.bookstore.users.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sn.ndiaye.bookstore.auth.services.AuthService;
import sn.ndiaye.bookstore.commons.ErrorDto;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.users.exceptions.EmailAlreadyTakenException;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.dtos.UserDto;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.mappers.UserMapper;
import sn.ndiaye.bookstore.users.services.UserService;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        var user = userService.createUser(request);
        var uri = uriComponentsBuilder
                .path("/users/{userId}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(userMapper.toDto(user));
    }

    @GetMapping
    public Iterable<UserDto> getAllUsers() {
        var users = userService.getAllUsers();
        return userMapper.toDtos(users);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") UUID id) {
        var user = userService.getUser(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/me")
    public UserDto getAuthUser() {
        var user = authService.getCurrentUser();
        return userMapper.toDto(user);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyTaken(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class, LoanNotFoundException.class})
    public ResponseEntity<Void> handleEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

}
