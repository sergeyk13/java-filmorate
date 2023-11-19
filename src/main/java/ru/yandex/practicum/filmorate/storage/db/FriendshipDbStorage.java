package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

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
}
