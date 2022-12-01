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
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

        int countFilms = films.size();

        assertThat(countFilms).isEqualTo(2);

    }

    @Test()
    @DisplayName("Поиск всех фильмов в БД.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void getAllFilmsTest() {

        Optional<Collection<Film>> filmsTest = Optional.ofNullable(filmDbStorage.getAllFilms());
        int countFilms = 0;

        if (filmsTest.isPresent()) {
            countFilms = filmsTest.get().size();
        }

        assertThat(countFilms).isEqualTo(2);

    }

    @Test
    @DisplayName("Поиск фильма по ид.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void getFilmByIdTest() {

        Long filmId = 1L;
        Optional<Film> filmFind =
                Optional.ofNullable(filmDbStorage.getFilmById(filmId));

        assertThat(filmFind).isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", filmId));

    }

    @Test()
    @DisplayName("Добавление нового фильма в БД.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void addFilmTest() {

        Film filmNew = new Film();
        Mpa mpaNew = new Mpa();
        mpaNew.setId(1L);
        mpaNew.setName("G");
        filmNew.setName("New_film_3");
        filmNew.setReleaseDate(LocalDate.of(2001, 1, 1));
        filmNew.setDescription("New film about nature");
        filmNew.setDuration(90);
        filmNew.setRate(4);
        filmNew.setMpa(mpaNew);
        filmDbStorage.add(filmNew);

        Long filmId = filmNew.getId();
        Optional<Film> filmFind =
                Optional.ofNullable(filmDbStorage.getFilmById(filmId));

        assertThat(filmFind).isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", filmId));

    }

    @Test()
    @DisplayName("Обновление фильма в БД.")
    @Sql({"classpath:table.sql", "classpath:data_table.sql"})
    void updateFilmTest() {

        Film filmNew = new Film();
        Mpa mpaNew = new Mpa();
        mpaNew.setId(4L);
        mpaNew.setName("R");
        filmNew.setId(1L);
        filmNew.setName("New_film_1_update");
        filmNew.setReleaseDate(LocalDate.of(2001, 1, 1));
        filmNew.setDescription("New film about nature and story");
        filmNew.setDuration(90);
        filmNew.setRate(4);
        filmNew.setMpa(mpaNew);
        filmDbStorage.update(filmNew);

        Long filmId = filmNew.getId();
        Optional<Film> filmFind =
                Optional.ofNullable(filmDbStorage.getFilmById(filmId));

        assertThat(filmFind).isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", filmId));

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
