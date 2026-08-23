package com.mick.movie.controller;

import com.mick.movie.dto.MovieDetails;
import com.mick.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
@CrossOrigin is enabled for local testing because each microservice runs on a different origin (port),
so the browser would otherwise block cross-origin requests. In production, this is handled by the API Gateway.
* */
@CrossOrigin
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDetails> getMovie(@PathVariable Integer movieId) {
        var movieDetails = this.movieService.getMovie(movieId);
        return ResponseEntity.ok(movieDetails);
    }

    @PostMapping
    public ResponseEntity<MovieDetails> saveMovie(@RequestBody MovieDetails request) {
        var movieDetails = this.movieService.saveMovie(request);
        return ResponseEntity.ok(movieDetails);
    }

}
