package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserService userService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
    }

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.getAllFilms();
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    public Film getFilmById(Long id) {
        return findFilmById(id);
    }

    public Film create(Film film) {
        if (filmStorage.isFindFilm(film)) {
            throw new AlreadyExistsException("Фильм с "
                    + film.getId() + " ид был добавлен ранее.");
        } else {
            if (checkReleaseDate(film)) {
                //film.setLikes(createLikesData(film));
                film.setLikes(Set.of());
                filmStorage.add(sortGenreData(film));
                createGenreData(film);
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
                //film.setLikes(createLikesData(film));
                film.setLikes(Set.of());
                deleteGenreData(film);
                filmStorage.update(sortGenreData(film));
                createGenreData(film);
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
        film = filmStorage.addLikes(filmId, film, userId);
        log.debug("Обновлен список лайков у фильма: {}", film);
        return film;
    }

    public Film deleteLikesFilm(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        userService.findUserById(userId);
        film = filmStorage.deleteLikes(filmId, film, userId);
        log.debug("Обновлен список лайков после удаления у фильма: {} ", film);
        return film;
    }

    public Collection<Film> getFilmsByCountLikes(int count) {
        return filmStorage.getFilmsByCountLikes(count);
    }

    public Film findFilmById(Long id) {
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

    private void createGenreData(Film film) {
        Set<Genre> genres = film.getGenres();
        Long filmID = film.getId();
        if (genres != null) {
            for (Genre genre : genres) {
                genreService.addGenreToFilm(filmID, genre.getId());
            }
        } else {
            film.setGenres(Set.of());
        }
    }

    private void deleteGenreData(Film film) {
        Set<Genre> genres = film.getGenres();
        Long filmID = film.getId();
        if (genres != null) {
                genreService.deleteGenreToFilm(filmID);
        }
    }

    private Film sortGenreData(Film film) {
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
           Set<Genre> genresSort =
                    new TreeSet<>(Comparator.comparing(Genre::getId));
            genresSort.addAll(genres);
            film.setGenres(genresSort);
        }
        return film;
    }

    private boolean checkReleaseDate(Film film) {
        return film.getReleaseDate().isAfter(
                LocalDate.of(1895, 12, 28));
    }

}