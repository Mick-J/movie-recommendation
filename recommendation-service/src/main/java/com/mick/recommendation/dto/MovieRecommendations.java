package com.mick.recommendation.dto;

import java.util.List;

public record MovieRecommendations(RecommendationType type,
                                   Integer customerId,
                                   List<MovieSummary> movies) {

    public static MovieRecommendations newlyAdded(List<MovieSummary> movies) {
        return new MovieRecommendations(RecommendationType.NEWLY_ADDED, null, movies);
    }

    public static MovieRecommendations personalized(Integer customerId, List<MovieSummary> movies) {
        return new MovieRecommendations(RecommendationType.PERSONALIZED, customerId, movies);
    }

}
