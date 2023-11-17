package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm);
    }

    @Override
    public Film findOne(int id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        List<Film> users = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, id);
        if (users.size() != 1) {
            throw new NotFoundException();
        } else return users.get(0);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILMS(TITLE, DESCRIPTION, RELEASE_DATE, DURATION) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getDescription());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET TITLE=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=? WHERE FILM_ID=?";

        int rowsUpdated = jdbcTemplate.update(sql,
                film.getTitle(),
                film.getDescription(),
                java.sql.Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getDuration(),
                film.getId());

        if (rowsUpdated > 0) {
            return film;
        } else {
            return null;
        }
    }

    private static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .title(rs.getString("TITLE"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(convertTimestampToLocalDate(rs.getTimestamp("RELEASE_DATE")))
                .duration(rs.getInt("DURATION"))
                .build();
    }

    private static LocalDate convertTimestampToLocalDate(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        throw new NotFoundException("Ошибка преобразования даты релиза");
    }
}
