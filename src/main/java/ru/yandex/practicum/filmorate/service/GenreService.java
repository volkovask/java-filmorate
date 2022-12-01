package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> findAll() {
        Collection<Genre> genres = genreStorage.getAllGenre();
        log.debug("Текущее количество жанров {}", genres.size());
        return genres;
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            throw new NotFoundException("Жанр не найден : " + genre);
        }
        log.debug("Найден жанр : {}", genre);
        return genre;
    }

    public void addGenreToFilm(Long filmId, Long genreId) {
        getGenreById(genreId);
        genreStorage.addGenreToFilm(filmId, genreId);
    }

    public void deleteGenreToFilm(Long filmId) {
        genreStorage.deleteGenreToFilm(filmId);
    }

}
