package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private long generateId = 0;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        Collection<User> users = userStorage.getAllUsers();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    public User getUserById(Long id) {
        return findUserById(id);
    }

    public Collection<User> getMyFriends(Long id) {
        User user = findUserById(id);
        Set<Long> friends = getUserFriendData(user);
        return getUserDataFromId(friends);
    }

    public Collection<User> getCommonFriendsOtherUser(Long id, Long otherId) {
        User user = findUserById(id);
        User userFriend = findUserById(otherId);
        Set<Long> commonFriends = new HashSet<>(getUserFriendData(user));
        Set<Long> friends = getUserFriendData(userFriend);
        commonFriends.retainAll(friends);
        return getUserDataFromId(commonFriends);
    }

    public User create(User user) {
        if (userStorage.isFindUser(user)) {
            throw new AlreadyExistsException("Пользователь с "
                    + user.getId() + " id был добавлен ранее.");
        } else {
            fillUserName(user);
            long userId = getGenerateId();
            user.setFriends(createFriendsData(user));
            user.setId(userId);
            userStorage.add(user);
            log.debug("Сохранен пользователь: {}", user);
            return user;
        }
    }

    public User update(User user) {
        long userId = user.getId();
        getUserById(userId);
        fillUserName(user);
        user.setFriends(createFriendsData(user));
        userStorage.update(user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    public User addInFriends(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        addFriend(id, user, friendId);
        addFriend(friendId, userFriend, id);
        return user;
    }

    public User deleteOnFriends(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend = findUserById(friendId);
        deleteFriend(id, user, friendId);
        deleteFriend(friendId, userFriend, id);
        return user;
    }

    private Set<Long> getUserFriendData(User user) {
        Set<Long> friends = user.getFriends();
        if (friends.size() == 0) {
            log.debug("Список друзей пуст: {}", user);
        }
        return friends;
    }

    private Collection<User> getUserDataFromId(Set<Long> friends) {
        List<User> users = new ArrayList<>();
        if (friends.size() != 0) {
            for (Long id : friends) {
                users.add(userStorage.getUserById(id));
            }
        }
        return users;
    }

    public User findUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден: " + user);
        }
        log.debug("Найден пользователь: {}", user);
        return user;
    }

    private User addFriend(Long id, User user, Long friendId) {
        Set<Long> friends = user.getFriends();
        friends.add(friendId);
        user.setFriends(friends);
        userStorage.add(user);
        log.debug("Обновлен список друзей у пользователя: {}", user);
        return user;
    }

    private Set<Long> createFriendsData(User user) {
        Set<Long> friends = user.getFriends();
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }

    private User deleteFriend(Long id, User user, Long friendId) {
        Set<Long> friends = user.getFriends();
        if (friends.size() != 0) {
            friends.remove(friendId);
            user.setFriends(friends);
            userStorage.add(user);
            log.debug("Обновлен список друзей после удаления у пользователя: {}", user);
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