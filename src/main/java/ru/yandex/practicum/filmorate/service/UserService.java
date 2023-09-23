package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FriendAlreasdyAddedExeprion;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    public void addFriend(User user,User secondUser ) throws FriendAlreasdyAddedExeprion {
        Set<Integer> friends = user.getFriendsId();
        Set<Integer> secondFriends = secondUser.getFriendsId();
        if (friends.contains(secondUser.getId()) || secondFriends.contains(user.getId())) {
            throw new FriendAlreasdyAddedExeprion("Friend is already added");
        } else {
            user.setFriendsId(secondUser.getId());
            secondUser.setFriendsId(user.getId());
        }
    }

    public void removeFromFriends(User user, User secondUser) throws FriendAlreasdyAddedExeprion {
        Set<Integer> friends = user.getFriendsId();
        Set<Integer> secondFriends = secondUser.getFriendsId();
        if (friends.contains(secondUser.getId()) || secondFriends.contains(user.getId())) {
            throw new FriendAlreasdyAddedExeprion("Friend is already added");
        } else {
            friends.remove(secondUser.getId());
            user.setFriendsId(friends);
            secondFriends.remove(user.getId());
            secondUser.setFriendsId(secondFriends);
        }
    }

    public Set<Integer> showCommonFriends(User user, User secondUser) {
        Set<Integer> commonFriends = new HashSet<>();
        user.getFriendsId().forEach(id ->
        {
            if (secondUser.getFriendsId().contains(id)) {
                commonFriends.add(id);
            }
        });
        return commonFriends;
    }
}
