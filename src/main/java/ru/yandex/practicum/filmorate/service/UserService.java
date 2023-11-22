package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FriendAlreasdyAddedExeption;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;

        this.friendshipStorage = friendshipStorage;
    }

    public void addFriends(Integer firstId, Integer secondId) throws FriendAlreasdyAddedExeption {
        User firstUser = userStorage.findOne(firstId);
        User secondUser = userStorage.findOne(secondId);

        addFriend(firstUser, secondId);
        friendshipStorage.addFriend(firstId, secondId);
        log.info("user: {} and user: {} start friendship", firstUser, secondId);
    }

    private void addFriend(User user, int friendId) {
            Set<Integer> friends = friendshipStorage.getFriendsID(user.getId());
            if (friends.contains(friendId)) {
                log.error("Friend is already added");
                throw new FriendAlreasdyAddedExeption("Friend is already added");
            } else {
                friends.add(friendId);
            }
    }

    public void removeFromFriends(User user, User secondUser) throws FriendAlreasdyAddedExeption {
        Set<Integer> friends = friendshipStorage.getFriendsID(user.getId());
        Set<Integer> secondFriends = friendshipStorage.getFriendsID(secondUser.getId());
        if (!(friends.contains(secondUser.getId()) || secondFriends.contains(user.getId()))) {
            throw new FriendAlreasdyAddedExeption("Friend is already added");
        } else {
            friendshipStorage.removeFriend(user.getId(), secondUser.getId());
            log.info("user: {} and user: {} friendship over", user.getId(), secondUser.getId());
        }
    }

    public List<User> showCommonFriends(User user, User secondUser) throws NotFoundException {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> commonSet = new HashSet<>();
        Set<Integer> commonFriendsFirstUser = friendshipStorage.getFriendsID(user.getId());
        Set<Integer> commonFriendsSecondUser = friendshipStorage.getFriendsID(secondUser.getId());
        if (commonFriendsFirstUser.isEmpty()) {
            return commonFriends;
        } else if (commonFriendsSecondUser.isEmpty()) {
            return commonFriends;
        } else {
            commonFriendsFirstUser.forEach(id -> {
                if (commonFriendsSecondUser.contains(id)) {
                    commonSet.add(id);
                }
            });
            log.info("Return common friends list");
            return returnFriends(commonSet);
        }
    }

    public List<User> returnFriends(Set<Integer> idList) throws NotFoundException {
        log.info("Return friends list");
        return idList.stream()
                .map(userStorage::findOne)
                .collect(Collectors.toList());
    }

    public User findOne(int id) throws NotFoundException {
        return userStorage.findOne(id);
    }

    public User create(User user) {
        if (user.getName().isBlank()) {
            log.info("Change name on login: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        log.info("Create User: {}", user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        userStorage.findOne(user.getId());
        log.info("Update User: {}", user);
        return userStorage.updateUser(user);
    }

    public Set<Integer> getFriendsId(int id) throws NotFoundException {
        log.info("Return friend's id list");
        return userStorage.getFriendsId(id);
    }

    public List<User> getAll() {
        log.info("Return all user list");
        return userStorage.getAll();
    }
}
