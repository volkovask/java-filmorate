package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.GenreStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM GENRE";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM GENRE WHERE ID = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        return Collections.singleton(jdbcTemplate.queryForObject(
                SQL_QUERY_SELECT_ALL, GenreStorageUtils::makeGenre));
    }

    @Override
    public Genre getGenreById(Long id) {
        List<Genre> genres = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                GenreStorageUtils::makeGenre);
        if (genres.size() != 1) {
            throw new NotFoundException("Жанр с таким " +
                    id + " ид отсутствует.");
        }
        return genres.get(FIRST_INDEX);
    }

}
