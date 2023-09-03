package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class FilmControllerTest {
    Film testFilm;
    private int id = 1;
    private String name = "testName";
    private String description = "test description";
    private LocalDate releaseDate = LocalDate.of(1991,1,1);
    private  int duration = 90;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private FilmController controller;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private List<Film> films;

    @BeforeEach
    void setUp(){
        testFilm = new Film(id,name,description,releaseDate,duration);
    }

    @Test
    void shouldBeGetAllUsersFindAll() {

        var userList = List.of(testFilm);
        doReturn(userList.get(0)).when(films).get(0);
        var response = controller.getFilms();

        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(userList.get(0), response.getBody().get(0));
    }

    @Test
    void shouldBeAddNewFilm() throws Exception {
        String json = objectMapper.writeValueAsString(testFilm);


        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void shouldBeNotCreateNewFilmWithoutName() throws Exception {
        Film testFilm2 = new Film(id,"",description,releaseDate,duration);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"Название фильма не может быть пустым\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldBeNotCreateNewFilmWithLongDuration() throws Exception {
        String longDescription = new String(new char[201]).replace('\0', 'A');
        Film testFilm2 = new Film(id,name,longDescription,releaseDate,duration);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"Длина описания превышает 200 символов\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldBeNotCreateNewFilmWithWrongTime() throws Exception {
        Film testFilm2 = new Film(id,name,description,LocalDate.of(1200,1,1),duration);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"дата релиза должна быть — не раньше 28 декабря 1895 года\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldBeNotCreateNewFilmWithWrongDuration() throws Exception {
        duration = -1;
        Film testFilm2 = new Film(id,name,description,releaseDate,duration);
        String json = objectMapper.writeValueAsString(testFilm2);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"Продолжительность фильма должна быть положительной.\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldBeUpdateFilmWith() throws Exception {
        String json = objectMapper.writeValueAsString(testFilm);
        Film testFilm2 = new Film(id,"newName",description,releaseDate,duration);
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
        String json = objectMapper.writeValueAsString(testFilm);

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"Фильм " + testFilm.getName() + " не существует\"",
                        mvcResult.getResolvedException().getMessage()));
    }

}