package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.utils.MpaStorageUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.List;

@Component
@Repository
@Qualifier("mpaDbStorage")
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final int FIRST_INDEX = 0;
    private final static String SQL_QUERY_SELECT_ALL = "SELECT * FROM MPA";
    private final static String SQL_QUERY_SELECT_ID = "SELECT * FROM MPA WHERE mpa_ID = ?";

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return jdbcTemplate.query(
                SQL_QUERY_SELECT_ALL, MpaStorageUtils::makeMpa);
    }

    @Override
    public Mpa getMpaById(Long id) {
        List<Mpa> mpas = jdbcTemplate.query(SQL_QUERY_SELECT_ID,
                MpaStorageUtils::makeMpa, id);
        if (mpas.size() != 1) {
            throw new NotFoundException("Рейтинг с таким " +
                    id + " ид отсутствует.");
        }
        return mpas.get(FIRST_INDEX);
    }

}
