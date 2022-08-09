package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email(message = "Неверный email.")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым и с пробелами.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата в будущем.")
    private LocalDate birthday;

}