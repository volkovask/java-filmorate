package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final static String SQL_QUERY_SELECT_ALL = "SELECT f.ID, " +
            "f.name, f.duration, f.description, " +
            "f.release_date, f.rate, f.mpa_ID, " +
            "m.mpa_ID, m.mpa_name " +
            "FROM FILMS AS f " +
            "LEFT JOIN MPA AS m ON f.mpa_ID = m.mpa_ID ";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FilmDbStorage filmDbStorage;

    @Test
    @DisplayName("Тестирование RowMapper and ResultSet")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void filmMapperTest() {
        List<Film> films = jdbcTemplate.query(SQL_QUERY_SELECT_ALL, new FilmMapper());
        System.out.println("Найдено " + films.size() + " фильмов");
    }


    @Test()
    @DisplayName("Вывода всех фильмов из БД. Ожидание 2")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void getAllFilmsTest() {
        Optional<Collection<Film>> filmsTest = Optional.ofNullable(filmDbStorage.getAllFilms());
        System.out.println("Найдено фильмов " + filmsTest.get());
        System.out.println("Найдено " + filmsTest.get().size() + " фильма");

    }

    @Test
    @DisplayName("Вывод фильма по ид.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void getFilmByIdTest() {

        Optional<Film> filmFind =
                Optional.ofNullable(filmDbStorage.getFilmById(1L));

        System.out.println("Найден фильм " + filmFind.get());
        System.out.println("Фильм ид " + filmFind.get().getId());

    }

    private static final class FilmMapper implements RowMapper<Film> {

        public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Film.builder()
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
        }
    }

}
