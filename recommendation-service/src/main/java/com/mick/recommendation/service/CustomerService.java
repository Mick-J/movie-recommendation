package com.mick.recommendation.service;

import com.mick.netflux.events.CustomerGenreUpdatedEvent;
import com.mick.recommendation.dto.RecommendationEvents;
import com.mick.recommendation.mapper.RecommendationMapper;
import com.mick.recommendation.repository.CustomerGenreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerGenreRepository customerGenreRepository;
    private final ApplicationEventPublisher applicationEventPublisher;;

    public CustomerService(CustomerGenreRepository customerGenreRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerGenreRepository = customerGenreRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public void updateGenre(CustomerGenreUpdatedEvent genreUpdatedEvent) {
        var entity = RecommendationMapper.toCustomerGenre(genreUpdatedEvent);
        customerGenreRepository.save(entity);
        applicationEventPublisher.publishEvent(
                new RecommendationEvents.PersonalizedEvent(genreUpdatedEvent.customerId())
        );
    }

}
