package com.mick.customer;

import com.mick.customer.dto.GenreUpdateRequest;
import com.mick.netflux.events.CustomerGenreUpdatedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@EnableTestBinder
@AutoConfigureRestTestClient
public class CustomerEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void genreUpdatedEvent() {
        GenreUpdateRequest request = new GenreUpdateRequest("Thriller");
        testClient.patch()
                .uri("/api/customers/2/genre")
                .body(request)
                .exchange()
                .expectStatus().isNoContent();

        // validate the event
        var message = this.outputDestination.receive(1000, "customer-events");
        var event = JsonMapper.shared()
                .readValue(message.getPayload(), CustomerGenreUpdatedEvent.class);

        Assertions.assertNotNull(event);
        Assertions.assertEquals(2, message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        Assertions.assertEquals(2,  event.customerId());
        Assertions.assertEquals("Thriller",  event.favoriteGenre());
    }

}
