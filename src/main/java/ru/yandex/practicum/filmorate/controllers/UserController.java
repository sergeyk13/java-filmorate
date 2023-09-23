package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping()
    public List<User> findAll() {
        return inMemoryUserStorage.getUsers();
    }

    @PostMapping()
    public User create(@RequestBody @Valid User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody @Valid User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable int id) throws NotFoundException {
        return inMemoryUserStorage.findOne(id);
    }
}