package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;

    private static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("TITLE"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(convertTimestampToLocalDate(rs.getTimestamp("RELEASE_DATE")))
                .duration(rs.getInt("DURATION"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("MPA"))
                        .build())
                .build();
    }

    private static LocalDate convertTimestampToLocalDate(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        throw new NotFoundException("Ошибка преобразования даты релиза");
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        List<Film> filmsNotReady = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm);
        List<Film> films = new ArrayList<>();
        for (Film film : filmsNotReady) {
            films.add(compareFilm(film));
        }
        return films;
    }

    @Override
    public Film findOne(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("film witch id: " + id + " not find");
        } else {
            return compareFilm(films.get(0));
        }
    }

    private boolean filmCheker(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, id);
        return films.size() == 1;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILMS(TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        return compareFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmCheker(film.getId())) {
            String sql = "UPDATE FILMS SET TITLE=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA=? WHERE ID=?";

            int rowsUpdated = jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    java.sql.Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            if (rowsUpdated > 0) {
                List<Genre> genresList = film.getGenres();
                Set<Integer> genreIdSet = new HashSet<>();
                if (genresList != null) {
                    if (!genresList.isEmpty()) {
                        filmGenreDbStorage.removeByFilmId(film.getId());

                        for (Genre genreId : genresList) {
                            genreIdSet.add(genreId.getId());
                        }
                        for (Integer id : genreIdSet) {
                            filmGenreDbStorage.add(film.getId(), id);
                        }

                    } else filmGenreDbStorage.removeByFilmId(film.getId());
                } else filmGenreDbStorage.removeByFilmId(film.getId());

            } else {
                throw new ValidationException("Обновление в фильма, ошибка параметра");
            }
        } else {
            throw new NotFoundException("film witch id: " + film.getId() + " not find");
        }
        return compareFilm(film);
    }

    private Film compareFilm(Film film) {
        List<Genre> genreList = new ArrayList<>();
        Set<Genre> genreSet = new HashSet<>();
        if (film.getGenres() == null) {
            List<Integer> genresIds = filmGenreDbStorage.getGenres(film.getId());
            if (genresIds == null || genresIds.isEmpty()) {
                film.setGenre(genreList);
            } else {
                for (Integer genresId : genresIds) {
                    genreList.addAll(genreDbStorage.findOne(genresId));
                }
            }

        } else {
            List<Genre> genreListWithId = film.getGenres();
            for (Genre genre : genreListWithId) {
                if (filmGenreDbStorage.checkExist(film.getId(), genre.getId())) {
                    genreSet.addAll(genreDbStorage.findOne(genre.getId()));
                } else {
                    filmGenreDbStorage.add(film.getId(), genre.getId());
                    genreSet.addAll(genreDbStorage.findOne(genre.getId()));
                }
            }
        }
        genreList.addAll(genreSet);
        film.setGenre(genreList);
        film.setMpa(mpaDbStorage.findOne(film.getMpa().getId()));
        return film;
    }
}
