package ru.yandex.practicum.filmorate.dao.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStorageUtils {

    private UserStorageUtils() {
    }

    public static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();


        return user;
    }

}
