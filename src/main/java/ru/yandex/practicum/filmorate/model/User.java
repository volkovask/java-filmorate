package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;
    @Email(message = "Неверно заполнен формат email.")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым и не должен содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата в будущем.")
    private LocalDate birthday;

}