package sn.ndiaye.bookstore.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.loans.entities.Loan;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<Loan> loans = new LinkedHashSet<>();

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean hasLoanedBook(Book book) {
        var bookLoanCount = loans.stream()
                .map(Loan::getBook)
                .map(Book::getId)
                .filter(bookId -> bookId.equals(book.getId()))
                .count();
        return bookLoanCount != 0;
    }
}
