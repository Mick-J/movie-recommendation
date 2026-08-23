package com.mick.recommendation.service;

import com.mick.netflux.events.MovieAddedEvent;
import com.mick.recommendation.dto.RecommendationEvents;
import com.mick.recommendation.mapper.RecommendationMapper;
import com.mick.recommendation.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MovieService(MovieRepository movieRepository,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.movieRepository = movieRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void addMovie(MovieAddedEvent movieAddedEvent) {
        var entity = RecommendationMapper.toMovie(movieAddedEvent);
        movieRepository.save(entity);
        applicationEventPublisher.publishEvent(
                new RecommendationEvents.NewMovieEvent(movieAddedEvent.movieId())
        );
    }

}
