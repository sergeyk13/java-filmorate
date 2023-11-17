merge into PUBLIC.GENRES (GENRE_ID, GENRE)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6,'Боевик');

merge into PUBLIC.RATING (RATING_ID,RATING)
    values (1, 'G'),
           (2, 'PG'),
           (3, 'PG=13'),
           (4, 'R'),
           (5, 'NC-17');