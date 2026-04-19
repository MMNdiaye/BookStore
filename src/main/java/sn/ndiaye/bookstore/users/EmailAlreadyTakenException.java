package sn.ndiaye.bookstore.users;


import lombok.Getter;

@Getter
public class EmailAlreadyTakenException extends RuntimeException {
    private final String email;

    public EmailAlreadyTakenException(String email) {
        super("This email is already taken");
        this.email = email;
    }
}
