package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty
    @Email
    private final String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "username must be of 6 to 12 length with no special characters")
    private final String login;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private final LocalDate birthday;
    private int id;
    private String name;
    private Set<Integer> friendsId = new HashSet<>();

    public void setFriendsId(Set<Integer> friendsId) {
        this.friendsId = friendsId;
    }

    public void setFriendsId(int friendsId) {
        this.friendsId.add(friendsId);
    }
}
