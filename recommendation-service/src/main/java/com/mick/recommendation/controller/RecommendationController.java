package com.mick.recommendation.controller;

import com.mick.recommendation.dto.MovieRecommendations;
import com.mick.recommendation.service.RecommendationService;
import com.mick.recommendation.service.RecommendationStreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationStreamService recommendationStreamService;
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationStreamService recommendationStreamService, RecommendationService recommendationService) {
        this.recommendationStreamService = recommendationStreamService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{customerId}")
    public List<MovieRecommendations> getRecommendations(@PathVariable Integer customerId) {
        return List.of(
                MovieRecommendations.newlyAdded(this.recommendationService.findNewlyAdded()),
                MovieRecommendations.personalized(customerId, this.recommendationService.findPersonalized(customerId))
        );
    }

    @GetMapping(value = "/{customerId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieRecommendations> getRecommendationStream(@PathVariable Integer customerId){
        return this.recommendationStreamService.streamRecommendations(customerId);
    }

}
