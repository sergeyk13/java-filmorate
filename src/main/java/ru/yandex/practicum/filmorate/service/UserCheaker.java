package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserCheaker {
    public static Boolean cheakUser(User user) throws ValidationException {
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        if (!email.contains("@")) {
            log.error("email error");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (login.isBlank()) {
            log.error("login error: login is blank");
            throw new ValidationException("login не может быть пустым");
        }
        if (name == null) {
            user.setName(login);
            log.info("Set name user:" + user.getName());
        } else if (name.isBlank()) {
            user.setName(login);
            log.info("Set name user:" + user.getName());
        }
        if (!birthday.isBefore(LocalDate.now())) {
            log.error("birthday error");
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
        return true;
    }
}
