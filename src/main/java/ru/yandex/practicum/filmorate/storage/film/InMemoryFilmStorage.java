package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return add(film);
    }

    @Override
    public boolean isFindFilm(Film film) {
        return films.containsKey(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public Film addLikes(Long id, Film film, Long userId) {
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        film.setRate(likes.size());
        add(film);
        return film;
    }

    @Override
    public Film deleteLikes(Long id, Film film, Long userId) {
        Set<Long> likes = film.getLikes();
        if (likes.size() != 0) {
            likes.remove(userId);
            film.setLikes(likes);
            film.setRate(likes.size());
            add(film);
        }
        return film;
    }

    @Override
    public Collection<Film> getFilmsByCountLikes(int count) {
        return getAllFilms()
                .stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}