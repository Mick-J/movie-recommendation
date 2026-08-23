package com.mick.customer.messaging;

import com.mick.netflux.events.CustomerGenreUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CustomerEventPublisher.class);
    private static final String CUSTOMER_EVENT_OUT = "customer-events-out";
    private final StreamBridge streamBridge;

    public CustomerEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @EventListener
    public void onGenreUpdatedEvent(CustomerGenreUpdatedEvent genreUpdatedEvent) {
        var message = MessageBuilder.withPayload(genreUpdatedEvent)
                .setHeader(KafkaHeaders.KEY, genreUpdatedEvent.customerId())
                .build();
        this.streamBridge.send(CUSTOMER_EVENT_OUT, message);
        log.info("published: {}", message);
    }

}
