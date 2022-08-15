package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidDataException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generateIdFilm = 0;

    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            throw new AlreadyExistsException("Фильм с "
                    + film.getId() + " ид был добавлен ранее.");
        } else {
            if (checkReleaseDate(film)) {
                int filmId = getGenerateIdFilm();
                film.setId(filmId);
                films.put(filmId, film);
                log.debug("Сохранен фильм " + film);
                return film;
            } else {
                throw new NotValidDataException("Дата позднее 28.12.1895 г.");
            }
        }
    }

    public Film update(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            if (checkReleaseDate(film)) {
                films.put(filmId, film);
                log.debug("Обновлен фильм " + film);
                return film;
            } else {
                throw new NotValidDataException("Дата позднее 28.12.1895 г.");
            }
        } else {
            throw new NotFoundException("Фильм с таким " +
                    filmId + " ид отсутствует.");
        }
    }


    private boolean checkReleaseDate(Film film) {
        return film.getReleaseDate().isAfter(
                LocalDate.of(1895, 12, 28));
    }

    private int getGenerateIdFilm() {
        return ++this.generateIdFilm;
    }

}
