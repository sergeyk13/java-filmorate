CREATE TABLE IF NOT EXISTS FILMS
(
    film_id      INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(200) NULL,
    release_date TIMESTAMP    NULL,
    duration     INT          NOT NULL,
    genre        VARCHAR      NULL,
    rating       VARCHAR      NULL
);

CREATE TABLE IF NOT EXISTS MPA
(
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    rating    VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_rating
(
    film_id   INT NOT NULL,
    rating_id INT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (rating_id) REFERENCES rating (rating_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    genre    VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  INT NOT NULL,
    genre_id INT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES GENRES (genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    login    VARCHAR(30) NOT NULL,
    email    VARCHAR(30) NOT NULL,
    birthday TIMESTAMP   NOT NULL,
    name     VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id   INT     NOT NULL,
    friend_id INT     NOT NULL,
    status    boolean NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
);


