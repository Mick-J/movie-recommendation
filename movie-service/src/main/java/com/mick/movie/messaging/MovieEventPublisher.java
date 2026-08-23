package com.mick.movie.messaging;

import com.mick.netflux.events.MovieAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MovieEventPublisher {
    public static final String MOVIE_EVENTS_OUT = "movie-events-out";
    private final Logger logger = LoggerFactory.getLogger(MovieEventPublisher.class);
    private final StreamBridge streamBridge;

    public MovieEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @EventListener
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMovieAdded(MovieAddedEvent movieAddedEvent) {
        Message<MovieAddedEvent> message = MessageBuilder.withPayload(movieAddedEvent)
                .setHeader(KafkaHeaders.KEY, movieAddedEvent.movieId())
                .build();

        this.streamBridge.send(MOVIE_EVENTS_OUT, message);
    }

}
