package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(int filmId, int genreId) {
        String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void addList(int filmId, List<Genre> genres) {
        String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) " +
                "values (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
    }

    @Override
    public List<Integer> getGenres(int filmId) {
        String sqlQuery = "select GENRE_ID from FILM_GENRE WHERE FILM_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        List<Integer> list = new ArrayList<>();

        while (rowSet.next()) {
            list.add((rowSet.getInt("GENRE_ID")));
        }
        return list;
    }

    @Override
    public void remove(int filmId, int genreId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?  AND GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void removeByFilmId(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public boolean checkExist(int filmId, int genreId) {
        String sqlQuery = "select * from FILM_GENRE WHERE FILM_ID = ?  AND GENRE_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId, genreId);
        return rowSet.next();
    }
}
