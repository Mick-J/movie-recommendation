package com.mick.recommendation.messaging;

import com.mick.netflux.events.CustomerGenreUpdatedEvent;
import com.mick.netflux.events.MovieAddedEvent;
import com.mick.recommendation.service.CustomerService;
import com.mick.recommendation.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {
    private static final Logger log = LoggerFactory.getLogger(EventConsumerConfig.class);

    @Bean
    Consumer<CustomerGenreUpdatedEvent> genreUpdatedEventConsumer(CustomerService customerService) {
        return withLogging(customerService::updateGenre);
    }

    @Bean
    Consumer<MovieAddedEvent>  movieAddedEventConsumer(MovieService movieService) {
        return withLogging(movieService::addMovie);
    }

    private <T> Consumer<T> withLogging( Consumer<T> consumer) {
        return t -> {
            log.info("received: {}" , t );
            consumer.accept(t);
        };
    }
}
