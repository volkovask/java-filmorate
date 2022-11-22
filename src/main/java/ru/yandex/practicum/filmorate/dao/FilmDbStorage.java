package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.utils.FilmStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {

        return null;
    }

    @Override
    public Film add(long filmId, Film film) {
        String sql = "INSERT INTO FILMS " +
                "(name, release_date, description, duration, rate, mpa_id)" +
                "values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public boolean isFindFilm(Film film) {
        return false;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM FILMS WHERE ID = ?";
        List<Film> film = jdbcTemplate.query(sql, FilmStorageUtils::makeFilm, id);
        if (film.size() != 1) {
            throw new NotFoundException("Фильм с таким " +
                    id + " ид отсутствует.");
        }
        return film.get(FIRST_INDEX);
        //System.out.println("Sql query " + sql + " id " + id);
        //List<Film> film = jdbcTemplate.query(sql, FilmStorageUtils::makeFilm, id);
        //System.out.println("Film " + film);
        //return jdbcTemplate.queryForObject(sql, FilmStorageUtils::makeFilm, id);
    }
}
