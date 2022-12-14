package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;


    @Test()
    @DisplayName("Вывод пользователей из БД по ID.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void getUserByIdTest() {
        Optional<User> usersOne = Optional.ofNullable(userDbStorage.getUserById(1L));
        System.out.println("Найден пользователь " + usersOne.get());

        assertThat(usersOne).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1L));

        Optional<User> usersTwo = Optional.ofNullable(userDbStorage.getUserById(2L));
        System.out.println("Найден пользователь " + usersTwo.get());

        assertThat(usersTwo).isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 2L));

    }

}
