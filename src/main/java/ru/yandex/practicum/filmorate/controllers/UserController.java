package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserCheaker;

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

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            UserCheaker.cheakUser(user);
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
            UserCheaker.cheakUser(user);

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