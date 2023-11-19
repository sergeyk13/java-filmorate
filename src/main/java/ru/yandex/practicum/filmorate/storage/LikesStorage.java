package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;

public interface LikesStorage {
    void addLike(int filmId, int userId);

    Set<Integer> getLikes(int filmId);

    void removeLike(int filmId, int userId);

    List<Integer> getTenPopular();
}
