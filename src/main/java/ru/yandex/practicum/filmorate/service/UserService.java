package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FriendAlreasdyAddedExeption;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(Integer firstId, Integer secondId) throws FriendAlreasdyAddedExeption {
        User firstUser = userStorage.findOne(firstId);
        User secondUser = userStorage.findOne(secondId);

        addFriend(firstUser, secondId);
        addFriend(secondUser, firstId);
        log.info("user: {} and user: {} start friendship", firstUser, secondId);
    }

    private void addFriend(User user, int friendId) {
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
            log.info("user: {} and user: {} friendship over", user.getId(), secondUser.getId());
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
            commonFriendsFirstUser.forEach(id -> {
                if (commonFriendsSecondUser.contains(id)) {
                    commonSet.add(id);
                }
            });
            return returnFriends(commonSet);
        }
    }

    public List<User> returnFriends(Set<Integer> idList) throws NotFoundException {
        return idList.stream()
                .map(userStorage::findOne)
                .collect(Collectors.toList());
    }

    public User findOne(int id) throws NotFoundException{
        return userStorage.findOne(id);
    }

    public User create(User user){
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Set<Integer> returnFriendsId(int id) throws NotFoundException {
        return userStorage.returnFriendsId(id);
    }

    public List<User> getAll(){
        return userStorage.getAll();
    }
}
