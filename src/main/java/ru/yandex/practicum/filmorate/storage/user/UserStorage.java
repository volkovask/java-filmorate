package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    Collection<User> getAllUsers();

    User add(User user);

    User update(User user);

    boolean isFindUser(User user);

    User getUserById(Long id);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    Collection<User> getMyFriends(Long id);

    Collection<User> getCommonFriendsOtherUser(Long id, Long otherId);

}