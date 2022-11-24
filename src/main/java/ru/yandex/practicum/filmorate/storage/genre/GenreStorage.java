package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    Collection<Genre> getAllGenre();

    Genre getGenreById(Long id);

    void addGenreToFilm(Long filmId, Long genreId);

    void deleteGenreToFilm(Long filmId, Long genreId);

    Collection<Genre> getGenresForFilm(Long filmId);

}
