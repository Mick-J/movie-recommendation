package com.mick.recommendation.service;

import com.mick.recommendation.dto.MovieSummary;
import com.mick.recommendation.mapper.RecommendationMapper;
import com.mick.recommendation.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final MovieRepository movieRepository;

    public RecommendationService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieSummary> findNewlyAdded() {
        return movieRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(RecommendationMapper::toMovieSummary)
                .toList();
    }

    public List<MovieSummary> findPersonalized(Integer customerId) {
        return movieRepository.findPersonalized(customerId)
                .stream()
                .map(RecommendationMapper::toMovieSummary)
                .toList();
    }

    public MovieSummary findMovie(Integer movieId) {
        return movieRepository.findById(movieId)
                .map(RecommendationMapper::toMovieSummary)
                .orElseThrow();
    }

}
