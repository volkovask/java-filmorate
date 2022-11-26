package ru.yandex.practicum.filmorate.dao.utils;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaStorageUtils {

    private MpaStorageUtils() {
    }

    public static Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        return mpa;
    }

}
