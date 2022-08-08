package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotEmpty(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Описание не больше 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Длительность не может быть отрицательной.")
    private int duration;

}
