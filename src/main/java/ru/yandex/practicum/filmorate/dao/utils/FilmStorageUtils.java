package ru.yandex.practicum.filmorate.dao.utils;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmStorageUtils {

    private FilmStorageUtils() {
    }

    public static Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setRate(resultSet.getInt("rate"));
        return film;
    }

}
