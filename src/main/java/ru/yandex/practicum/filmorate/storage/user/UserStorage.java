package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    Collection<User> getAllUsers();

    User add(long userId, User user);

    boolean isFindUser(User user);

    User getUserById(Long id);

}