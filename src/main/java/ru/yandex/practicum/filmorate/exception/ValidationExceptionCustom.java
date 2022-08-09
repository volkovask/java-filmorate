package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class ValidationExceptionCustom extends ValidationException {
    public ValidationExceptionCustom(String str) {
        super(str);
    }
}
