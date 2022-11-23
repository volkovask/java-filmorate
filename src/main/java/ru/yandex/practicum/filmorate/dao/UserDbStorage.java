package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.UserStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_INSERT = "INSERT INTO USERS " +
            "(email, login, name , birthday)" +
            "values(?, ?, ?, ?)";
    private final static String SQL_QUERY_UPDATE = "UPDATE USERS SET " +
            "email = ?, login =?, name = ?, birthday = ? " +
            "WHERE ID = ?";
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM USERS";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM USERS WHERE ID = ?";


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAllUsers() {
        return Collections.singleton(jdbcTemplate.queryForObject(
                SQL_QUERY_SELECT_ALL, UserStorageUtils::makeUser));
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(
                    SQL_QUERY_INSERT, new String[]{"id"});
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getLogin());
            stm.setString(3, user.getName());
            stm.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return stm;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_QUERY_UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public boolean isFindUser(User user) {
        long id = user.getId();
        List<User> users = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                UserStorageUtils::makeUser, id);
        return users.size() == 1;
    }

    @Override
    public User getUserById(Long id) {
        List<User> users = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                UserStorageUtils::makeUser, id);
        if (users.size() != 1) {
            throw new NotFoundException("Пользователь с таким " +
                    id + " ид отсутствует.");
        }
        return users.get(FIRST_INDEX);
    }
}
