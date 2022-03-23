package com.github.flooooooooooorian.meinkochbuch.controllers.exception;

import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipeDoesNotExistException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.RecipePrivacyForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeDoesNotExistException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(RecipeDoesNotExistException ex) {
        log.error("Resource not found!", ex);

        ApiError apiError = new ApiError("Resource not found!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipePrivacyForbiddenException.class)
    public ResponseEntity<ApiError> handleRecipePrivacyForbidden(RecipePrivacyForbiddenException ex) {
        log.error("Access to Recipe denied!", ex);

        ApiError apiError = new ApiError("Access to Recipe denied!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleUnknownException(Throwable ex) {
        log.error("Unknown Error!", ex);

        ApiError apiError = new ApiError("Unknown Error!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
