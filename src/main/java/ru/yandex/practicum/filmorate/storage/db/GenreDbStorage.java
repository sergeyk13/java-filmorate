package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    private static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("GENRE"))
                .build();
    }

    @Override
    public List<Genre> findOne(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genreList = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, id);
        if (genreList.size() != 1) {
            throw new NotFoundException();
        } else return genreList;
    }

    @Override
    public Genre getOne(int id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genreList = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, id);
        if (genreList.size() != 1) {
            throw new NotFoundException();
        } else return genreList.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Genre> list = new ArrayList<>();

        while (rowSet.next()) {
            list.add(new Genre(rowSet.getInt("id"), rowSet.getString("genre")));
        }
        if (list.isEmpty()) {
            throw new NotFoundException();
        } else {
            return list;
        }
    }
}
