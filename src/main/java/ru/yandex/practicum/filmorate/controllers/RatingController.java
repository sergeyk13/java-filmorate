package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {

    private final RatingService service;
    @GetMapping
    public List<Rating> getAll() {
        final List<Rating> ratings = service.getAll();
        log.info("получение всех рейтингов");
        return ratings;
    }

    @GetMapping("/{id}")
    public Rating getById(@PathVariable int id ) {
        log.info("получение рейтинга по id: {}", id);
        return service.getById(id);
    }
}
