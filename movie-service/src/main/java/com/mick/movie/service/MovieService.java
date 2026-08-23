package com.mick.movie.service;


import com.mick.movie.dto.MovieDetails;
import com.mick.movie.exception.MovieNotFoundException;
import com.mick.movie.mapper.MovieMapper;
import com.mick.movie.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    @Value("${KAFKA_TRUSTSTORE_PASSWORD:NOT_SET}")
    private String debugPw;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher eventPublisher) {
        this.movieRepository = movieRepository;
        this.eventPublisher = eventPublisher;
    }

    public MovieDetails getMovie(Integer movieId) {
         log.info("Resolved truststore password: [{}]", debugPw);

        return this.movieRepository.findById(movieId)
                .map(MovieMapper::toMovieDetails)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
    }

    @Transactional
    public MovieDetails saveMovie(MovieDetails movieDetails) {
        var movie = this.movieRepository.save(MovieMapper.toMovie(movieDetails));
        this.eventPublisher.publishEvent(MovieMapper.toMovieAddedEvent(movie));
        return MovieMapper.toMovieDetails(movie);
    }

}
