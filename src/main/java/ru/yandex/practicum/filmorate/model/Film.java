package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Set<Long> likes;
    @Id
    private long id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    private LocalDate releaseDate;
    @Size(max = 200, message = "Описание не больше 200 символов.")
    private String description;
    @Positive(message = "Длительность не может быть отрицательной.")
    private int duration;
    private int rate;
    private Mpa mpa;
    private Set<Genre> genres;

}