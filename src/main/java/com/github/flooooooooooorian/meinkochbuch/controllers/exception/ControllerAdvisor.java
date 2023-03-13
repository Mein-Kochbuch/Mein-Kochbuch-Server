package com.github.flooooooooooorian.meinkochbuch.controllers.exception;

import com.github.flooooooooooorian.meinkochbuch.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeDoesNotExistException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(RecipeDoesNotExistException ex) {
        log.error("Recipe not found!", ex);

        ApiError apiError = new ApiError("Recipe not found!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipePrivacyForbiddenException.class)
    public ResponseEntity<ApiError> handleRecipePrivacyForbidden(RecipePrivacyForbiddenException ex) {
        log.error("Access to Recipe denied!", ex);

        ApiError apiError = new ApiError("Access to Recipe denied!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CookbookDoesNotExist.class)
    public ResponseEntity<ApiError> handleCookbookDoesNotExist(CookbookDoesNotExist ex) {
        log.error("Cookbook not found!", ex);

        ApiError apiError = new ApiError("Cookbook not found!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CookPrivacyForbiddenException.class)
    public ResponseEntity<ApiError> handleCookPrivacyForbiddenException(CookPrivacyForbiddenException ex) {
        log.error("Access to Cookbook denied!", ex);

        ApiError apiError = new ApiError("Access to Cookbook denied!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleLoginAccountDisabled(DisabledException ex) {
        log.error("Account disabled!", ex);

        ApiError apiError = new ApiError("Account disabled!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleLoginBadCredentials(BadCredentialsException ex) {
        log.error("Bad credentials!", ex);

        ApiError apiError = new ApiError("Bad credentials", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RatingFailedException.class)
    public ResponseEntity<ApiError> handleRatingFailedException(RatingFailedException ex) {
        log.error("Rating failed!", ex);

        ApiError apiError = new ApiError("Rating failed!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecipeEditForbiddenException.class)
    public ResponseEntity<ApiError> handleRecipeEditForbiddenException(RecipeEditForbiddenException ex) {
        log.error("Recipe Edit forbidden! Not Owner of Recipe!", ex);

        ApiError apiError = new ApiError("Recipe Edit forbidden! Not Owner of Recipe!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied!", ex);

        ApiError apiError = new ApiError("Access denied!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ApiError> handleAccessInvalidFileException(InvalidFileException ex) {
        log.error("Reading File failed!", ex);

        ApiError apiError = new ApiError("Image Upload Exception", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleUnknownException(Throwable ex) {
        log.error("Unknown Error!", ex);

        ApiError apiError = new ApiError("Unknown Error!", ex.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
