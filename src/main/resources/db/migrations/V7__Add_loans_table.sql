CREATE TABLE loans(
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    book_id BIGINT REFERENCES books(id) NOT NULL,
    user_id UUID REFERENCES users(id) NOT NULL,
    taken_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_in_days INT NOT NULL DEFAULT 0 CHECK(duration_in_days > 0),
    initial_fee DECIMAL(10, 2) NOT NULL CHECK(initial_fee >= 0),
    rate_per_day DECIMAL(10, 2) NOT NULL CHECK(initial_fee >= 0),
    UNIQUE(book_id, user_id)
);