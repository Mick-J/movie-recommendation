package com.mick.movie.exception;

public class MovieNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Movie [id=%d] is not found";

    public MovieNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
