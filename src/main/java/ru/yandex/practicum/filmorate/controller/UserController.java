package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new AlreadyExistsException("Пользователь с "
                    + user.getId() + " id был добавлен ранее.");
        } else {
            fillUserName(user);
            int userId = getGenerateId();
            user.setId(userId);
            users.put(userId, user);
            log.debug("Сохранен пользователь " + user);
            return user;
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int userId = user.getId();
        if (users.containsKey(userId)) {
            fillUserName(user);
            users.put(userId, user);
            log.debug("Пользователь обновлен " + user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с таким " + userId +
                    " ид отсутствует.");
        }
    }

    private User fillUserName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private int getGenerateId() {
        return ++this.generateId;
    }

}