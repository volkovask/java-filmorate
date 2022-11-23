package ru.yandex.practicum.filmorate.dao.utils;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaStorageUtils {

    private MpaStorageUtils() {
    }

    public static Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    }

}
