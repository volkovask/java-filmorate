package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Set<Long> friends;
    @Id
    private long id;
    @Email(message = "Неверно заполнен формат email.")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым и не должен содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата в будущем.")
    private LocalDate birthday;

}