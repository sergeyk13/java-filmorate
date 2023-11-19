package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FriendAlreasdyAddedExeption;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> findAll() {
        return userService.getAll();
    }

    @PostMapping()
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable int id) throws NotFoundException {
        return userService.findOne(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) throws NotFoundException, FriendAlreasdyAddedExeption {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable int id, @PathVariable int friendId) throws NotFoundException, FriendAlreasdyAddedExeption {
        userService.removeFromFriends(userService.findOne(id), userService.findOne(friendId));
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable int id) throws NotFoundException {
        return userService.returnFriends(userService.getFriendsId(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundException {
        return userService.showCommonFriends(userService.findOne(id), userService.findOne(otherId));
    }
}