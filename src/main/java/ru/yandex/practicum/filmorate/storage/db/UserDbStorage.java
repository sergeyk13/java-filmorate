package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private static User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(convertTimestampToLocalDate(rs.getTimestamp("birthday")))
                .name(rs.getString("name"))
                .build();
    }

    private static LocalDate convertTimestampToLocalDate(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        throw new NotFoundException("Ошибка преобразования даты рождения");
    }

    private static Friendship createFriendship(ResultSet rs, int rowNum) throws SQLException {
        return Friendship.builder()
                .friendId(rs.getInt("FRIEND_ID"))
                .status(rs.getBoolean("STATUS"))
                .build();
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser);
    }

    @Override
    public User findOne(int id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, id);
        if (users.size() != 1) {
            throw new NotFoundException("user witch id: " + id + " not find");
        } else {
            User user = users.get(0);
            return user;
        }
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into USERS(LOGIN, EMAIL, BIRTHDAY, NAME) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(user.getBirthday().atStartOfDay()));
            stmt.setString(4, user.getName());
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET login=?, email=?, birthday=?, name=? WHERE id=?";

        int rowsUpdated = jdbcTemplate.update(sql,
                user.getLogin(),
                user.getEmail(),
                java.sql.Timestamp.valueOf(user.getBirthday().atStartOfDay()),
                user.getName(),
                user.getId());

        if (rowsUpdated > 0) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Set<Integer> getFriendsId(int userId) {
        Set<Integer> friendsIds = new HashSet<>();
        try {
            String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE user_id = ?";

            jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("friend_id"), userId)
                    .forEach(friendsIds::add);
            return friendsIds;
        } catch (NotFoundException e) {
            log.error("Get friends id for user: " + userId + " unpassable");
        }
        return friendsIds;
    }

    public HashMap<Integer, Boolean> getFriendship(int userId) {
        String sqlQuery = "SELECT FRIEND_ID, STATUS  FROM FRIENDSHIP WHERE user_id = ?";
        List<Friendship> friendships = jdbcTemplate.query(sqlQuery, UserDbStorage::createFriendship, userId);
        HashMap<Integer, Boolean> friendship = new HashMap<>();
        if (friendships.isEmpty()) {
            return friendship;
        } else {
            for (Friendship f : friendships) {
                friendship.put(f.getFriendId(), f.isStatus());
            }
            return friendship;
        }
    }
}
