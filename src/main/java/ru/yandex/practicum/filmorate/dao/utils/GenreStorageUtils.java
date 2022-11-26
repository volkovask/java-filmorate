package ru.yandex.practicum.filmorate.dao.utils;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreStorageUtils {

    private GenreStorageUtils() {
    }

    public static Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }

}
