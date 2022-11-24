package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.GenreStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;

@Component
@Qualifier("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM GENRE";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM GENRE WHERE ID = ?";
    private final static String SQL_QUERY_INSERT_GENRE = "INSERT INTO FILM_GENRE " +
            "(film_ID, genre_ID) VALUES (?, ?)";
    private final static String SQL_QUERY_DELETE_GENRE = "DELETE FROM FILM_GENRE " +
            "WHERE film_ID = ? AND genre_ID = ?";
    private final static String SQL_QUERY_SELECT_GENRES = "SELECT " +
            "g.genre_ID, ge.name " +
            "FROM FILM_GENRE AS g " +
            "LEFT JOIN GENRE AS ge ON g.genre_ID = ge.ID WHERE film_ID = ? " +
            "GROUP BY g.genre_ID";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        return jdbcTemplate.query(
                SQL_QUERY_SELECT_ALL, GenreStorageUtils::makeGenre);
    }

    @Override
    public Genre getGenreById(Long id) {
        List<Genre> genres = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                GenreStorageUtils::makeGenre, id);
        if (genres.size() != 1) {
            throw new NotFoundException("Жанр с таким " +
                    id + " ид отсутствует.");
        }
        return genres.get(FIRST_INDEX);
    }

    @Override
    public void addGenreToFilm(Long filmId, Long genreId) {
        jdbcTemplate.update(SQL_QUERY_INSERT_GENRE, filmId, genreId);
    }

    @Override
    public void deleteGenreToFilm(Long filmId, Long genreId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_GENRE, filmId, genreId);
    }

    @Override
    public Collection<Genre> getGenresForFilm(Long filmId) {
        return jdbcTemplate.query(SQL_QUERY_SELECT_GENRES,
                GenreStorageUtils::makeGenre, filmId);
    }


}
