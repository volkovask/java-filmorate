package ru.yandex.practicum.filmorate.dao.utils;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class FilmStorageUtils {

    private static GenreStorage genreStorage;

    private FilmStorageUtils(GenreStorage genreStorage) {
        FilmStorageUtils.genreStorage = genreStorage;
    }

    public static Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .build();
        film.setGenres(sortedGenre(film.getId()));
        return film;
    }

    private static Set<Genre> sortedGenre(Long id) {
        Optional<List<Genre>> genres =
                Optional.ofNullable(genreStorage.getGenresForFilm(id));
        if (genres.isPresent()) {
            Set<Genre> genresSort =
                    new TreeSet<>(Comparator.comparing(Genre::getId));
            genresSort.addAll(genres.get());
            return genresSort;
        } else {
            return Set.of();
        }
    }

}
