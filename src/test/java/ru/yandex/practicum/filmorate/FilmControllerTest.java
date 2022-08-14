package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private static final String URI = "/films";

    @Test
    public void getAllRecordsForEmptyData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addNewFilmForEmptyData() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("")
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addNewFilmForIncorrectlyFilledInFields() throws Exception {
        Film film = Film.builder()
                .id(-1)
                .name("    ")
                .description("    ")
                .releaseDate(LocalDate.of(1890, 10, 10))
                .duration(-1)
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

}
