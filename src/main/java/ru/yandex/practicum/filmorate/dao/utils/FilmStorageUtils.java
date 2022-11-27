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
        this.genreStorage = genreStorage;
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
        long id = film.getId();
        film.setGenres(sortedGenre(genreStorage.getGenresForFilm(id)));
        return film;
    }

    private static Set<Genre> sortedGenre(List<Genre> genres) {
            Set<Genre> genresSort =
                    new TreeSet<>(Comparator.comparing(Genre::getId));
            genresSort.addAll(genres);
            return genresSort;
    }

}
