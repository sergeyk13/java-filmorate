package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.db.RatingDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingDbStorage storage;

    public List<Rating> getAll() {
        return storage.getAll();
    }

    public Rating getById(int id) {
        return storage.findOne(id);
    }
}
