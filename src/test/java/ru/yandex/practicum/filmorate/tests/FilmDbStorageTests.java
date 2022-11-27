package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.JdbcH2Runner;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@ContextConfiguration(classes = FilmDbStorage.class)
public class FilmDbStorageTests extends JdbcH2Runner {

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
    @Sql({"classpath:table.sql", "classpath:data_table.sql", "classpath:data_films.sql"})
    void filmMapperTest() {
        List<Film> films = jdbcTemplate.query(SQL_QUERY_SELECT_ALL, new FilmMapper());
        System.out.println(films);
    }

    @Test
    @Sql({"classpath:table.sql", "classpath:data_table.sql", "classpath:data_films.sql"})
    void getAllFilmsTest() {
        Collection<Film> filmsTest = filmDbStorage.getAllFilms();
        System.out.println(filmsTest);
    }


    private static final class FilmMapper implements RowMapper<Film> {

        public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
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
            return film;
        }
    }

}
