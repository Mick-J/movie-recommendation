INSERT INTO movie (id, title, vote_count, genres) VALUES
    (1, 'The Matrix', 17500, ARRAY['Action','Sci-Fi']),
    (2, 'Inception', 22100, ARRAY['Action','Adventure','Sci-Fi']),
    (3, 'The Godfather', 16200, ARRAY['Crime','Drama']),
    (4, 'Toy Story', 13500, ARRAY['Animation','Comedy','Family']),
    (5, 'Titanic', 19800, ARRAY['Drama','Romance']),
    (6, 'Jurassic Park', 14900, ARRAY['Adventure','Sci-Fi','Thriller']),
    (7, 'The Dark Knight', 24500, ARRAY['Action','Crime','Drama']),
    (8, 'The Shawshank Redemption', 21000, ARRAY['Drama']),
    (9, 'Avengers: Endgame', 23000, ARRAY['Action','Adventure','Sci-Fi']);

INSERT INTO customer_genre (customer_id, favorite_genre) VALUES
    (1, 'Action'),
    (2, 'Adventure');
