package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film add(long filmId, Film film);

    boolean isFindFilm(Film film);

    Film getFilmById(Long id);

}
