package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class AlreadyExistsException extends ValidationException {

    public AlreadyExistsException(String str) {
        super(str);
    }

}
