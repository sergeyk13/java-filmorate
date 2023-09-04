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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private User testUser;
    private int id = 1;
    private String email = "test@example.com";
    private String login = "testlogin";
    private String name = "Test User";
    private LocalDate birthday = LocalDate.of(2001, 1, 1);
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private List<User> users;

    @BeforeEach
    void setUp() {
        testUser = new User(1, "test@example.com", "testlogin", "Test User", LocalDate.of(2001, 1, 1));
    }

    @Test
    void shouldBeGetAllUsersFindAll() {

        var userList = List.of(testUser);
        doReturn(userList.get(0)).when(users).get(0);
        var response = userController.findAll();

        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(userList.get(0), response.getBody().get(0));
    }

    @Test
    void shouldBeCreateNewUser() throws Exception {
        String json = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void shouldBeCreateNewUserWithoutName() throws Exception {
        User testUser2 = new User(id, email, login, "", birthday);
        String json = objectMapper.writeValueAsString(testUser2);
        testUser2.setName(login);
        String checkedJson = objectMapper.writeValueAsString(testUser2);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(checkedJson));
    }

    @Test
    void shouldNotBeCreateNewUserWithoutRightEmail() throws Exception {
        User testUser2 = new User(id, "email", login, "", birthday);
        String json = objectMapper.writeValueAsString(testUser2);


        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"электронная почта не может быть пустой и должна содержать символ @\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldNotBeCreateNewUserWithBlankEmail() throws Exception {
        User testUser2 = new User(id, "   ", login, "", birthday);
        String json = objectMapper.writeValueAsString(testUser2);


        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"электронная почта не может быть пустой и должна содержать символ @\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldNotBeCreateNewUserWithBlankLogin() throws Exception {
        User testUser2 = new User(id, email, "  ", "", birthday);
        String json = objectMapper.writeValueAsString(testUser2);


        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"login не может быть пустым\"",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    void shouldNotBeCreateNewUserWithFutureBirthday() throws Exception {
        User testUser2 = new User(id, email, login, "", LocalDate.of(2025, 1, 1));
        String json = objectMapper.writeValueAsString(testUser2);


        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"дата рождения не может быть в будущем.\"",
                        mvcResult.getResolvedException().getMessage()));
    }


    @Test
    void shouldBeUpdateUser() throws Exception {
        String json = objectMapper.writeValueAsString(testUser);
        User testUser2 = new User(id, email, "newLogin", name, birthday);
        String json2 = objectMapper.writeValueAsString(testUser2);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
    }

    @Test
    void shouldNotBeUpdateUserNonExistentUser() throws Exception {
        String json = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertEquals("500 INTERNAL_SERVER_ERROR " +
                                "\"Пользователь не существует\"",
                        mvcResult.getResolvedException().getMessage()));
    }
}
