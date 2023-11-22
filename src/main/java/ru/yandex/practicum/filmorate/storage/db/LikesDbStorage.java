package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into LIKES(FILM_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Set<Integer> getLikes(int filmId) {
        String sqlQuery = "select USER_ID from LIKES WHERE FILM_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        Set<Integer> list = new HashSet<>();

        while (rowSet.next()) {
            list.add((rowSet.getInt("USER_ID")));
        }
        return list;
    }


    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ?  AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Integer> getTenPopular(int count) {
        String sqlQuery = "SELECT FILM_ID, COUNT(FILM_ID) as likeCount FROM LIKES " +
                "GROUP BY FILM_ID ORDER BY likeCount DESC LIMIT ?";
        List<Integer> listId = jdbcTemplate.query(sqlQuery,
                (resultSet, rowNum) -> resultSet.getInt("FILM_ID"), count);
        int n = count - listId.size();
        if (n != 0) {
            sqlQuery = "SELECT id FROM FILMS ORDER BY title LIMIT ?";
            listId.addAll(jdbcTemplate.query(sqlQuery,
                    (resultSet, rowNum) -> resultSet.getInt("ID"), n));
        }
        return listId;
    }

}
