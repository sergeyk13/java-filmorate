package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage storage;

    public List<Mpa> getAll() {
        log.info("Return all rating");
        return storage.getAll();
    }

    public Mpa getById(int id) {
        log.info("Return rating by id: {}", id);
        return storage.findOne(id);
    }
}
