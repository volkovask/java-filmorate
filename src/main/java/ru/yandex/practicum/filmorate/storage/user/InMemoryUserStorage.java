package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User add(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public boolean isFindUser(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

}
