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
        log.info("Request get all users");
        return userService.getAll();
    }

    @PostMapping()
    public User create(@RequestBody @Valid User user) {
        log.info("Request create user");
        return userService.create(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Request update user");
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable int id) throws NotFoundException {
        log.info("Request find one");
        return userService.findOne(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) throws NotFoundException, FriendAlreasdyAddedExeption {
        log.info("Request add friend");
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) throws NotFoundException, FriendAlreasdyAddedExeption {
        log.info("Request remove from friend");
        userService.removeFromFriends(userService.findOne(id), userService.findOne(friendId));
    }

    @GetMapping("{id}/friends")
    public List<User> returnFriends(@PathVariable int id) throws NotFoundException {
        log.info("Request get friend");
        return userService.returnFriends(userService.getFriendsId(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundException {
        log.info("Request get common friend");
        return userService.showCommonFriends(userService.findOne(id), userService.findOne(otherId));
    }
}