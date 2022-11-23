package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.getAllFilms();
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден: " + film);
        }
        log.debug("Найден фильм: {}", film);
        return film;
    }

    public Film create(Film film) {
        if (filmStorage.isFindFilm(film)) {
            throw new AlreadyExistsException("Фильм с "
                    + film.getId() + " ид был добавлен ранее.");
        } else {
            if (checkReleaseDate(film)) {
                film.setLikes(createLikesData(film));
                filmStorage.add(film);
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
                film.setLikes(createLikesData(film));
                filmStorage.update(film);
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

    public Film addLikesFilm(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        userService.findUserById(userId);
        addLikes(filmId, film, userId);
        return film;
    }

    public Film deleteLikesFilm(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        userService.findUserById(userId);
        deleteLikes(filmId, film, userId);
        return film;
    }

    public Collection<Film> getFilmsByCountLikes(int count) {
        return filmStorage
                .getAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film addLikes(Long id, Film film, Long userId) {
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        film.setRate(likes.size());
        filmStorage.add(film);
        log.debug("Обновлен список лайков у фильма: {}", film);
        return film;
    }

    private Film deleteLikes(Long id, Film film, Long userId) {
        Set<Long> likes = film.getLikes();
        if (likes.size() != 0) {
            likes.remove(userId);
            film.setLikes(likes);
            film.setRate(likes.size());
            filmStorage.add(film);
            log.debug("Обновлен список лайков после удаления у фильма: {} ", film);
        }
        return film;
    }

    private Film findFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден: " + film);
        }
        log.debug("Найден фильм: {}", film);
        return film;
    }

    private Set<Long> createLikesData(Film film) {
        Set<Long> likes = film.getLikes();
        if (likes == null) {
            likes = new HashSet<>();
        }
        return likes;
    }

    private boolean checkReleaseDate(Film film) {
        return film.getReleaseDate().isAfter(
                LocalDate.of(1895, 12, 28));
    }

}