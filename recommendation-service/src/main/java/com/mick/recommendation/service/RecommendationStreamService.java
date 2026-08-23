package com.mick.recommendation.service;

import com.mick.recommendation.dto.MovieRecommendations;
import com.mick.recommendation.dto.RecommendationEvents;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendationStreamService { // RecommendationNotifier
    private final Sinks.Many<MovieRecommendations> recommendationSink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE);
    private final Flux<MovieRecommendations> recommendationsFlux = recommendationSink.asFlux();
    private final RecommendationService recommendationService;

    public RecommendationStreamService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    public Flux<MovieRecommendations> streamRecommendations(Integer customerId){
        return recommendationSink.asFlux()
                .filter(rec -> Objects.isNull(rec.customerId()) || rec.customerId().equals(customerId));
    }

    @Async
    @EventListener // global
    public void onMovieAdded(RecommendationEvents.NewMovieEvent event){
        var movie = recommendationService.findMovie(event.movieId());
        var recommendations = MovieRecommendations.newlyAdded(List.of(movie));
        this.recommendationSink.emitNext(recommendations, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }

    @Async
    @EventListener
    public void onPersonalized(RecommendationEvents.PersonalizedEvent event){
        var personalized = recommendationService.findPersonalized(event.customerId());
        var recommendations = MovieRecommendations.personalized(event.customerId(), personalized);
        this.recommendationSink.emitNext(recommendations, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1)));
    }

}

