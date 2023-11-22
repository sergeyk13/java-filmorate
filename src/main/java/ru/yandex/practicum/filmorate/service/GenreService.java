package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage storage;

    public List<Genre> getAll() {
        log.info("Get all genres");
        return storage.getAll();
    }

    public Genre getOne(int id) {
        log.info("Get one genre");
        return storage.getOne(id);
    }

    public List<Genre> getById(int id) {
        log.info("Find genre for film by id: {}", id);
        return storage.findOne(id);
    }
}
