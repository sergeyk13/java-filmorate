package ru.yandex.practicum.filmorate.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drop")
public class DropController {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @GetMapping
    public void removeAll() {
        jdbcTemplate.execute("drop table FILM_GENRE;\n" +
                "\n" +
                "drop table FILM_RATING;\n" +
                "\n" +
                "drop table FRIENDSHIP;\n" +
                "\n" +
                "drop table GENRES;\n" +
                "\n" +
                "drop table LIKES;\n" +
                "\n" +
                "drop table FILMS;\n" +
                "\n" +
                "drop table RATING;\n" +
                "\n" +
                "drop table USERS;\n" +
                "\n");
    }
}
