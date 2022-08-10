package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private static final String URI = "/users";

    @Test
    public void getAllRecordsForEmptyData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addNewFilmForEmptyData() throws Exception {
        User user = User.builder()
                .email("")
                .login("")
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(user));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addNewFilmForIncorrectlyFilledInFields() throws Exception {
        User user = User.builder()
                .id(-1)
                .email("email")
                .login("test login")
                .name("")
                .birthday(LocalDate.of(2025, 10, 10))
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(user));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

}
