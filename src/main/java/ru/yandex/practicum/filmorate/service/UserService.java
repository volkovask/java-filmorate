package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private long generateId = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        Collection<User> users = userStorage.getAllUsers();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    public User getUserById(Long id) {
        User user = findUserById(id);
        return user;
    }

    public Collection<User> getMyFriends(Long id) {
        User user = findUserById(id);
        Set<Long> friends = user.getFriends();
        if (friends.size() != 0) {
            Collection<User> users = new ArrayList<>();
            for (Long friend : friends) {
                users.add(userStorage.getUserById(friend));
            }
            return users;
        }
        return null;
    }

    public Collection<User> getCommonFriendsOtherUser(Long id, Long otherId) {
        User user = findUserById(id);
        User userFriend = findUserById(otherId);


        return null;
    }

    public User create(User user) {
        if (userStorage.isFindUser(user)) {
            throw new AlreadyExistsException("Пользователь с "
                    + user.getId() + " id был добавлен ранее.");
        } else {
            fillUserName(user);
            long userId = getGenerateId();
            user.setId(userId);
            userStorage.add(userId, user);
            log.debug("Сохранен пользователь: {}", user);
            return user;
        }
    }

    public User update(User user) {
        long userId = user.getId();
        if (userStorage.isFindUser(user)) {
            fillUserName(user);
            userStorage.add(userId, user);
            log.debug("Обновлен пользователь: {}", user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с таким " + userId +
                    " ид отсутствует.");
        }
    }

    public User addInFriends(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        user = addFriend(id, user, friendId);
        addFriend(friendId, userFriend, id);
        return user;
    }

    public User deleteOnFriends(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        user = deleteFriend(id, user, friendId);
        deleteFriend(friendId, userFriend, id);
        return user;
    }

    private User findUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден: " + user);
        }
        log.debug("Найден пользователь: {}", user);
        return user;
    }

    private User addFriend(Long id, User user, Long friendId) {
        Set<Long> friends = user.getFriends();
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(friendId);
        user.setFriends(friends);
        userStorage.add(id, user);
        log.debug("Обновлен список друзей у пользователя: {}", user);
        return user;
    }

    private User deleteFriend(Long id, User user, Long friendId) {
        Set<Long> friends = user.getFriends();
        if (friends.size() != 0) {
            friends.remove(friendId);
            user.setFriends(friends);
            userStorage.add(id, user);
            log.debug("Обновлен список друзей у пользователя: {} после удаления", user);
        }
        return user;
    }

    private User fillUserName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private long getGenerateId() {
        return ++this.generateId;
    }

}
