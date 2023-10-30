package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty
    @Email
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "username must be of 6 to 12 length with no special characters")
    @NotBlank
    private final String login;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @PastOrPresent
    private LocalDate birthday;
    private int id;
    private String name;
    @Getter
    private Set<Integer> friendsId;

    public void setFriendsId(Set<Integer> friendsId) {
        this.friendsId = friendsId;
    }
}