package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film add(Film film);

    Film update(Film film);

    boolean isFindFilm(Film film);

    Film getFilmById(Long id);

    Film addLikes(Long id, Film film, Long userId);

    Film deleteLikes(Long id, Film film, Long userId);

    Collection<Film> getFilmsByCountLikes(int count);

}