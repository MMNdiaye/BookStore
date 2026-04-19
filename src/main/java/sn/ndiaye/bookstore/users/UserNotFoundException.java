package sn.ndiaye.bookstore.users;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final UUID id;

    public UserNotFoundException(UUID id) {
        super("User is not found");
        this.id = id;
    }
}
