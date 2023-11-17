package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findOne(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (rowSet.next()) {
            return new Genre(rowSet.getInt("genre_id"), rowSet.getString("genre"));
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Genre> list = new ArrayList<>();

        while (rowSet.next()) {
            list.add(new Genre(rowSet.getInt("genre_id"), rowSet.getString("genre")));
        }
        if (list.isEmpty()) {
            throw new NotFoundException();
        } else {
            return list;
        }
    }
}
