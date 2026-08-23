package com.mick.movie.controller.advice;

import com.mick.movie.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleException(MovieNotFoundException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Movie Not Found");
        return problem;
    }

}
