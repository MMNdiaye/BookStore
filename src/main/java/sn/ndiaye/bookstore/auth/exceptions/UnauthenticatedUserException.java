package sn.ndiaye.bookstore.auth.exceptions;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException() {
        super("There is no authenticated user");
    }
}
