package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private long generateIdFilm = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.getAllFilms();
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    public Film create(Film film) {
        if (filmStorage.isFindFilm(film)) {
            throw new AlreadyExistsException("Фильм с "
                    + film.getId() + " ид был добавлен ранее.");
        } else {
            if (checkReleaseDate(film)) {
                long filmId = getGenerateIdFilm();
                film.setId(filmId);
                filmStorage.add(filmId, film);
                log.debug("Сохранен фильм " + film);
                return film;
            } else {
                throw new NotValidDataException("Дата позднее 28.12.1895 г.");
            }
        }
    }

    public Film update(Film film) {
        long filmId = film.getId();
        if (filmStorage.isFindFilm(film)) {
            if (checkReleaseDate(film)) {
                filmStorage.add(filmId, film);
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

    private long getGenerateIdFilm() {
        return ++this.generateIdFilm;
    }

}
