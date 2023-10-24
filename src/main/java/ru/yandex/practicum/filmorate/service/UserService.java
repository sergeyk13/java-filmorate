package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FriendAlreasdyAddedExeption;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(Integer firstId, Integer secondId) throws FriendAlreasdyAddedExeption {
        User firstUser = userStorage.findOne(firstId);
        User secondUser = userStorage.findOne(secondId);

        addFrend(firstUser, secondId);
        addFrend(secondUser, firstId);
        log.info("user: " + firstId + " and user: " + secondId +
                " start friendship");
    }

    private void addFrend(User user, int friendId) {
        if (user.getFriendsId() != null) {
            Set<Integer> friends = user.getFriendsId();
            if (friends.contains(friendId)) {
                log.error("Friend is already added");
                throw new FriendAlreasdyAddedExeption("Friend is already added");
            } else {
                friends.add(friendId);
            }
        } else {
            Set<Integer> newList = new HashSet<>();
            newList.add(friendId);
            user.setFriendsId(newList);
        }
    }

    public void removeFromFriends(User user, User secondUser) throws FriendAlreasdyAddedExeption {
        Set<Integer> friends = user.getFriendsId();
        Set<Integer> secondFriends = secondUser.getFriendsId();
        if (!(friends.contains(secondUser.getId()) || secondFriends.contains(user.getId()))) {
            throw new FriendAlreasdyAddedExeption("Friend is already added");
        } else {
            friends.remove(secondUser.getId());
            user.setFriendsId(friends);
            secondFriends.remove(user.getId());
            secondUser.setFriendsId(secondFriends);
            log.info("user: " + user.getId() + " and user: " + secondUser.getId() +
                    " friendship over");
        }
    }

    public List<User> showCommonFriends(User user, User secondUser) throws NotFoundException {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> commonSet = new HashSet<>();
        Set<Integer> commonFriendsFirstUser = user.getFriendsId();
        Set<Integer> commonFriendsSecondUser = secondUser.getFriendsId();
        if (commonFriendsFirstUser == null) {
            return commonFriends;
        } else if (commonFriendsSecondUser == null) {
            return commonFriends;
        } else {
            commonFriendsFirstUser.forEach(id ->
            {
                if (commonFriendsSecondUser.contains(id)) {
                    commonSet.add(id);
                }
            });
            return returnFriends(commonSet);
        }
    }

    public List<User> returnFriends(Set<Integer> idList) throws NotFoundException {
        List<User> friends = idList.stream()
                .map(userStorage::findOne)
                .collect(Collectors.toList());
        return friends;
    }
}
