//package ru.yandex.practicum.filmorate.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.web.server.ResponseStatusException;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//    private int id = 1;
//    private String email = "test@example.com";
//    private String login = "testlogin";
//    private String name = "Test User";
//    private LocalDate birthday = LocalDate.of(2001, 1, 1);
//    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
//    private Set<Integer> friendsId;
//
//    private User testUser = new User(login,email,birthday,id,name,friendsId);
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private InMemoryUserStorage inMemoryUserStorage;
//    @MockBean
//    private UserService userService;
//
//    @Test
//    void shouldBeGetAllUsersFindAll() throws Exception {
//
//        var userList = List.of(testUser);
//        when(this.inMemoryUserStorage.getUsers()).thenReturn(userList);
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void findOneShouldReturnValidFilm() throws Exception {
//        var list = List.of(testUser);
//        when(this.inMemoryUserStorage.findOne(1)).thenReturn(list.get(0));
//
//        mockMvc.perform(get("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.login").value(login))
//                .andExpect(jsonPath("$.email").value(email));
//    }
//
//    @Test
//    void shouldBeCreateNewUser() throws Exception {
//        String json = objectMapper.writeValueAsString(testUser);
//        when(inMemoryUserStorage.create(any(User.class))).thenReturn(testUser);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().json(json));
//    }
//
//    @Test
//    void shouldBeCreateNewUserWithoutName() throws Exception {
//        User testUser2 = new User(login,email,birthday,id,"",friendsId);
//        String json = objectMapper.writeValueAsString(testUser2);
//        testUser2.setName(login);
//        String checkedJson = objectMapper.writeValueAsString(testUser2);
//        when(inMemoryUserStorage.create(any(User.class))).thenReturn(testUser2);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().json(checkedJson));
//    }
//
//    @Test
//    void shouldNotBeCreateNewUserWithoutRightEmail() throws Exception {
//        User testUser2 = new User("email",login,birthday,id,name,friendsId);
//        String json = objectMapper.writeValueAsString(testUser2);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldNotBeCreateNewUserWithBlankEmail() throws Exception {
//        User testUser2 = new User("",login,birthday,id,name,friendsId);
//        String json = objectMapper.writeValueAsString(testUser2);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldNotBeCreateNewUserWithBlankLogin() throws Exception {
//        User testUser2 = new User(email,"",birthday,id,name,friendsId);
//        String json = objectMapper.writeValueAsString(testUser2);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldNotBeCreateNewUserWithFutureBirthday() throws Exception {
//        User testUser2 = new User(email,login,LocalDate.of(2222,1,1),id,name,friendsId);
//        String json = objectMapper.writeValueAsString(testUser2);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    @Test
//    void shouldBeUpdateUser() throws Exception {
//        String json = objectMapper.writeValueAsString(testUser);
//        User testUser2 = new User(login,email,birthday,id,"newName",friendsId);
//        String json2 = objectMapper.writeValueAsString(testUser2);
//        when(inMemoryUserStorage.updateUser(any(User.class))).thenReturn(testUser2);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json));
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json2))
//                .andExpect(status().isOk())
//                .andExpect(content().json(json2));
//    }
//
//    @Test
//    void shouldNotBeUpdateUserNonExistentUser() throws Exception {
//        when(inMemoryUserStorage.updateUser(any(User.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                "Фильм " + testUser.getLogin() + " не существует"));
//
//        String json = objectMapper.writeValueAsString(testUser);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest());
//    }
//}
