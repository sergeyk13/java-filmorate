package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    int id = 0;
    private List<User> users = new ArrayList<>();

    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users);
    }

    private Boolean cheakUser(User user) throws ValidationException {
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

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            cheakUser(user);
            for (User u : users) {
                if (u.getEmail().equals(user.getEmail())) {
                    log.error("user's email exist");
                    throw new ValidationException("Пользователь с таким email уже существует!");
                }
            }
            user.setId(++id);
            users.add(user);
            log.info("User add: " + user.getEmail());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user);
        } catch (ValidationException e) {
            log.error("Error adding user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            boolean isExist = false;
            cheakUser(user);

            List<User> updatedUsers = new ArrayList<>();

            for (User existingUser : users) {
                if (existingUser.getId() == user.getId()) {
                    updatedUsers.add(user);
                    isExist = true;
                } else {
                    updatedUsers.add(existingUser);
                }
            }

            if (!isExist) {
                log.error("Error updating user: user isn't exist");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Пользователь не существует");
            }
            log.info("User update: " + user.getEmail());
            users = updatedUsers;
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user);

        } catch (ValidationException e) {
            log.error("Error updating user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}