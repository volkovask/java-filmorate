package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.utils.GenreStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Repository
@Qualifier("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM GENRE";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM GENRE WHERE genre_ID = ?";
    private final static String SQL_QUERY_INSERT_GENRE = "INSERT INTO FILM_GENRE " +
            "(film_ID, genre_ID) VALUES (?, ?)";
    private final static String SQL_QUERY_DELETE_GENRE = "DELETE FROM FILM_GENRE " +
            "WHERE film_ID = ?";
    private static final String SQL_QUERY_SELECT_GENRES = "SELECT " +
            "GENRE.GENRE_ID AS id, GENRE.GENRE_NAME AS name " +
            "FROM FILM_GENRE " +
            "LEFT JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.GENRE_ID " +
            "WHERE FILM_GENRE.FILM_ID = ? " +
            "ORDER BY GENRE.GENRE_ID ASC";

    @Autowired
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
    public void deleteGenreToFilm(Long filmId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_GENRE, filmId);
    }

    @Override
    public List<Genre> getGenresForFilm(Long filmId) {
        Optional<List<Genre>> genres =
                Optional.of(jdbcTemplate.query(SQL_QUERY_SELECT_GENRES,
                        GenreStorageUtils::makeGenre, filmId));
        return genres.get();
    }

}
