package ru.practicum.shareit.exception;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorHandlerTest {

    private final ErrorHandler errorHandler;

    @Test
    void validationExceptionTest() {
        ErrorResponse error = errorHandler.handleNotFound(new NotFoundException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void conflictExceptionTest() {
        ErrorResponse error = errorHandler.handleBadRequest(new ValidationException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void nullPointerExceptionTest() {
        ErrorResponse error = errorHandler.handleExistError(new WrongAccessException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void runtimeExceptionTest() {
        ErrorResponse error = errorHandler.handleExistError(new WrongStateException("message"));
        Assertions.assertEquals(error.getError(), "message");
    }

    @Test
    void sQLExceptionTest() {
        ErrorResponse error = errorHandler.handleThrowable(new Throwable("message"));
        Assertions.assertEquals(error.getError(), "message");
    }
}