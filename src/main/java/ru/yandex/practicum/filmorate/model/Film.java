package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private Set<Long> likes;
    private long id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Описание не больше 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Длительность не может быть отрицательной.")
    private int duration;

}
