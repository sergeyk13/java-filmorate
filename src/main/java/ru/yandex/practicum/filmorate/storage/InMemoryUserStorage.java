package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Getter
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private List<User> users = new ArrayList<>();

    public User findOne(int id) throws NotFoundException {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow(NotFoundException::new);
    }

    public User create(User user) {
        try {
            for (User u : users) {
                if (u.getEmail().equals(user.getEmail())) {
                    log.error("user's email exist");
                    throw new ValidationException("Пользователь с таким email уже существует!");
                }
            }
            if (user.getName() == null) {
                user.setName(user.getLogin());
                log.info("Set name user:" + user.getName());
            } else if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Set name user:" + user.getName());
            }
            user.setId(++id);
            users.add(user);
            log.info("User add: " + user.getEmail());
            return user;
        } catch (ValidationException e) {
            log.error("Error adding user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public User updateUser(User user) {

        boolean isExist = false;
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
        return user;
    }

    public Set<Integer> returnFriendsId(int id) throws NotFoundException {
        return findOne(id).getFriendsId();
    }
}