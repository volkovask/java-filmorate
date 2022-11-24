package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.utils.FilmStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_INSERT = "INSERT INTO FILMS " +
            "(name, release_date, description, duration, rate, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private final static String SQL_QUERY_UPDATE = "UPDATE FILMS SET " +
            "name = ?, release_date =?, description = ?, duration = ?, " +
            "rate = ?, mpa_id = ? " +
            "WHERE ID = ?";
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM FILMS";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM FILMS WHERE ID = ?";
    private final static String SQL_QUERY_INSERT_LIKES = "INSERT INTO FILM_LIKES " +
            "(film_ID, user_ID) VALUES (?, ?)";
    private final static String SQL_QUERY_DELETE_LIKES = "DELETE FROM FILM_LIKES " +
            "WHERE film_ID = ? AND user_ID = ?";
    private final static String SQL_QUERY_SELECT_TOP_FILMS = "SELECT TOP ? f.ID, " +
            "f.name, f.release_date, f.description, f.duration, f.rate, " +
            "m.name AS MPA_name FROM FILMS AS f " +
            "LEFT JOIN MPA AS m ON f.mpa_ID = m.ID " +
            "LEFT JOIN FILM_LIKES AS l ON f.ID = l.film_ID " +
            "GROUP BY f.ID " +
            "ORDER BY COUNT(l.film_ID) DESC";

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(SQL_QUERY_SELECT_ALL,
                FilmStorageUtils::makeFilm);
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(
                    SQL_QUERY_INSERT, new String[]{"id"});
            stm.setString(1, film.getName());
            stm.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
            stm.setString(3, film.getDescription());
            stm.setInt(4, film.getDuration());
            stm.setInt(5, film.getRate());
            stm.setLong(6, film.getMpa().getId());
            return stm;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_QUERY_UPDATE,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public boolean isFindFilm(Film film) {
        long id = film.getId();
        List<Film> films = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                FilmStorageUtils::makeFilm, id);
        return films.size() == 1;
    }

    @Override
    public Film getFilmById(Long id) {
        List<Film> films = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                FilmStorageUtils::makeFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("Фильм с таким " +
                    id + " ид отсутствует.");
        }
        return films.get(FIRST_INDEX);
    }

    @Override
    public Film addLikes(Long id, Film film, Long userId) {
        jdbcTemplate.update(SQL_QUERY_INSERT_LIKES, id, userId);
        return film;
    }

    @Override
    public Film deleteLikes(Long id, Film film, Long userId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_LIKES, id, userId);
        return film;
    }

    @Override
    public Collection<Film> getFilmsByCountLikes(int count) {
        return jdbcTemplate.query(SQL_QUERY_SELECT_TOP_FILMS,
                FilmStorageUtils::makeFilm, count);
    }

}
