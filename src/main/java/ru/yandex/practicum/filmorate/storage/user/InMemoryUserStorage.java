package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        return add(user);
    }

    @Override
    public boolean isFindUser(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        Set<Long> friends = user.getFriends();
        friends.add(friendId);
        user.setFriends(friends);
        add(user);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = getUserById(id);
        Set<Long> friends = user.getFriends();
        if (friends.size() != 0) {
            friends.remove(friendId);
            user.setFriends(friends);
            add(user);
        }
    }

    @Override
    public Collection<User> getMyFriends(Long id) {
        User user = getUserById(id);
        Set<Long> friends = getUserFriendData(user);
        return getUserDataFromId(friends);
    }

    @Override
    public Collection<User> getCommonFriendsOtherUser(Long id, Long otherId) {
        User user = getUserById(id);
        User userFriend = getUserById(otherId);
        Set<Long> commonFriends = new HashSet<>(getUserFriendData(user));
        Set<Long> friends = getUserFriendData(userFriend);
        commonFriends.retainAll(friends);
        return getUserDataFromId(commonFriends);
    }

    private Set<Long> getUserFriendData(User user) {
        return user.getFriends();
    }

    private Collection<User> getUserDataFromId(Set<Long> friends) {
        List<User> users = new ArrayList<>();
        if (friends.size() != 0) {
            for (Long id : friends) {
                users.add(getUserById(id));
            }
        }
        return users;
    }

}