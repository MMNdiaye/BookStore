CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE editions(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE books(
    id BIGSERIAL PRIMARY KEY,
    isbn VARCHAR(17),
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    edition_id BIGINT REFERENCES editions(id) NOT NULL,
    published_at DATE NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE genres(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE book_genres(
    book_id BIGINT REFERENCES books(id),
    genre_id BIGINT REFERENCES genres(id),
    PRIMARY KEY(book_id, genre_id)
);