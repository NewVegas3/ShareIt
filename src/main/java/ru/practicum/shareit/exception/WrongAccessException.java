package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongAccessException extends RuntimeException {
    public WrongAccessException(String message) {
        super(message);
    }
}
