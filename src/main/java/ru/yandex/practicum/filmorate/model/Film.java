package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {

    @NotBlank
    private String title;
    @Size(max = 200)
    private final String description;
    @MinimumDate
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate releaseDate;
    @Min(1)
    private final int duration;
    private int id;
    private Set<Integer> likes;
    private List<Genre> genres;
    private List<Rating> rating;

    public Film(String name, String description, LocalDate releaseDate, int duration, int id, Set<Integer> likes) {
        this.title = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
        this.likes = new HashSet<>();
    }

    public void addLike(int userId) {
        this.likes.add(userId);
    }
}
