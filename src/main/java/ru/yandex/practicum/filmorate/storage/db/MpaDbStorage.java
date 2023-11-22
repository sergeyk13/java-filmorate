package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    private static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .build();
    }

    @Override
    public Mpa findOne(int id) {
        String sqlQuery = "SELECT * FROM MPA WHERE ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (rowSet.next()) {
            return new Mpa(rowSet.getInt("id"), rowSet.getString("NAME"));
        } else {
            throw new NotFoundException("Rating id: " + id);
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from MPA";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Mpa> list = new ArrayList<>();

        while (rowSet.next()) {
            list.add(new Mpa(rowSet.getInt("id"), rowSet.getString("name")));
        }
        if (list.isEmpty()) {
            throw new NotFoundException("Ratings not found in table MPA");
        } else {
            return list;
        }
    }
}
