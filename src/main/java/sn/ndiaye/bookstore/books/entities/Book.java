package sn.ndiaye.bookstore.books.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name="published_at")
    private LocalDate publishedAt;

    @Column(name = "quantity")
    private Long quantity;

    @Builder.Default
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeGenre(String genreName) {
        for (var genre : genres)
            if (genreName.equals(genre.getName())) {
                genres.remove(genre);
                return;
            }
    }

    public boolean hasGenre(Genre genre) {
        return genres.contains(genre);
    }

    public boolean hasAvailableCopies() {
        return quantity > 0;
    }

    public void addQuantity(Long qtyToAdd) {
        quantity+=qtyToAdd;
    }

    public void reduceQuantity(Long qtyToRemove) {
        if (qtyToRemove > quantity)
            throw new IllegalArgumentException("Not enough stock");
        quantity -= qtyToRemove;
    }
}
