package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(
            @PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikesFilm(@PathVariable("id") Long id,
                             @PathVariable("userId") Long userId) {
        return filmService.addLikesFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikesFilm(@PathVariable("id") Long id,
                                @PathVariable("userId") Long userId) {
        return filmService.deleteLikesFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilmsByCountLikes(
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        return filmService.getFilmsByCountLikes(count);
    }

}