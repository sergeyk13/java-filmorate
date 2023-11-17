package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.storage.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements Mpa {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ru.yandex.practicum.filmorate.model.Mpa findOne(int id) {
        String sqlQuery = "SELECT * FROM RATING WHERE RATING_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (rowSet.next()) {
            return new ru.yandex.practicum.filmorate.model.Mpa(rowSet.getInt("genre_id"), rowSet.getString("genre"));
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<ru.yandex.practicum.filmorate.model.Mpa> getAll() {
        String sqlQuery = "select * from RATING";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        List<ru.yandex.practicum.filmorate.model.Mpa> list = new ArrayList<>();

        while (rowSet.next()) {
            list.add(new ru.yandex.practicum.filmorate.model.Mpa(rowSet.getInt("rating_id"), rowSet.getString("rating")));
        }
        if (list.isEmpty()) {
            throw new NotFoundException();
        } else {
            return list;
        }
    }
}
