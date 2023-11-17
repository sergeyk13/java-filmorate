package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    private int id = 1;
    private String name = "testName";
    private String description = "test description";
    private LocalDate releaseDate = LocalDate.of(1991, 1, 1);
    private int duration = 90;
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private Set<Integer> likes;

    Film testFilm = new Film(name, description, releaseDate, duration, id, likes);

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmDbStorage filmDbStorage;
    @MockBean
    private FilmService filmService;

    @Test
    void shouldBeGetAllFilmFindAll() throws Exception {

        var filmList = List.of(testFilm);
        when(this.filmDbStorage.getAll()).thenReturn(filmList);

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldBeAddNewFilm() throws Exception {
        String json = objectMapper.writeValueAsString(testFilm);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void findOneShouldReturnValidFilm() throws Exception {
        var filmList = List.of(testFilm);
        when(this.filmDbStorage.findOne(1)).thenReturn(filmList.get(0));

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.duration").value(duration));
    }

    @Test
    void shouldBeNotCreateNewFilmWithoutName() throws Exception {
        Film testFilm2 = new Film("", description, releaseDate, duration, id, likes);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeNotCreateNewFilmWithLongDuration() throws Exception {
        String longDescription = new String(new char[201]).replace('\0', 'A');
        Film testFilm2 = new Film(name, longDescription, releaseDate, duration, id, likes);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeNotCreateNewFilmWithWrongTime() throws Exception {
        Film testFilm2 = new Film(name, description, LocalDate.of(1, 1, 1), duration, id, likes);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeNotCreateNewFilmWithWrongDuration() throws Exception {
        duration = -1;
        Film testFilm2 = new Film(name, description, releaseDate, duration, id, likes);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeUpdateFilmWith() throws Exception {
        String json = objectMapper.writeValueAsString(testFilm);
        Film testFilm2 = new Film("newName", description, releaseDate, duration, id, likes);
        String json2 = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
    }

    @Test
    void shouldBeNotBeUpdateNotExistFilm() throws Exception {
        when(filmDbStorage.updateFilm(any(Film.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Фильм " + testFilm.getTitle() + " не существует"));

        String json = objectMapper.writeValueAsString(testFilm);

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}