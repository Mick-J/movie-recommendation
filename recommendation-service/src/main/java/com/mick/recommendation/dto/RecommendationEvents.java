package com.mick.recommendation.dto;

public class RecommendationEvents { // acts as namespace

    public record NewMovieEvent(Integer movieId) {

    }

    public record PersonalizedEvent(Integer customerId){

    }

}
