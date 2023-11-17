package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;
    @GetMapping
    public List<Genre> getAll() {
        final List<Genre>  genres = service.getAll();
        log.info("получение всех жанров");
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id ) {
        log.info("получение жанра по id: {}", id);
        return service.getById(id);
    }
}