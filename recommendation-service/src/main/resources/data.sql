DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS customer_genre;

CREATE TABLE movie (
    id INTEGER PRIMARY KEY,
    title TEXT,
    vote_count INTEGER,
    release_date VARCHAR(50),
    runtime INTEGER,
    poster_path TEXT,
    genres CHARACTER VARYING ARRAY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_genre (
    customer_id INTEGER PRIMARY KEY,
    favorite_genre VARCHAR(255)
);