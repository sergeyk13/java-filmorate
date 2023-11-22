package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, false);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE (USER_ID = ? AND FRIEND_ID = ?) OR (USER_ID = ? AND FRIEND_ID = ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, friendId, userId);
    }

    @Override
    public Set<Integer> getFriendsID(int userId) {
        String sqlQuery = "select FRIEND_ID from FRIENDSHIP WHERE USER_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        Set<Integer> list = new HashSet<>();

        while (rowSet.next()) {
            list.add((rowSet.getInt("FRIEND_ID")));
        }
        return list;
    }
}
