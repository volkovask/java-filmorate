package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> findAll() {
        Collection<Mpa> mpas = mpaStorage.getAllMpa();
        log.debug("Текущее количество рейтингов: {}", mpas.size());
        return mpas;
    }

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaStorage.getMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("Рейтинг не найден: " + mpa);
        }
        log.debug("Найден рейтинг: {}", mpa);
        return mpa;
    }

}
