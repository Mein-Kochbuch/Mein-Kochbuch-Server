package com.github.flooooooooooorian.meinkochbuch.exceptions;


public class RatingFailedException extends RuntimeException {

    public RatingFailedException(String ex, Throwable exception) {
        super(ex);
    }

}
