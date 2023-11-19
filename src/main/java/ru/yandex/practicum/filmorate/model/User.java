package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "username must be of 6 to 12 length with no special characters")
    @NotBlank
    private String login;
    @NotEmpty
    @Email
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private LocalDate birthday;
    private int id;
    private String name;
    @Getter
    private Set<Integer> friendsId;
    private HashMap<Integer, Boolean> friendship;

    public User() {
        this.friendsId = new HashSet<>();
        this.friendship = new HashMap<>();
    }

    public User(String login, String email, LocalDate birthday, String name) {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.name = name;
        this.friendsId = new HashSet<>();
        this.friendship = new HashMap<>();
    }

    public void setFriendsId(Set<Integer> friendsId) {
        this.friendsId = friendsId;
    }

    public void setFriendship(HashMap<Integer, Boolean> friendship) {
        this.friendship = friendship;
    }
}