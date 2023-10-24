package ru.yandex.practicum.filmorate.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
        log.error("NotFoundException");
    }

    public NotFoundException() {
        super();
        log.error("NotFoundException");
    }
}
