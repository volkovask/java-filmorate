package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class NotValidDataException extends ValidationException {

    public NotValidDataException(String str) {
        super(str);
    }
}
