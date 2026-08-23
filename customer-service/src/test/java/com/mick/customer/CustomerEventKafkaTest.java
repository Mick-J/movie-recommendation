package com.mick.customer;

import com.mick.customer.dto.GenreUpdateRequest;
import com.mick.netflux.events.CustomerGenreUpdatedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AutoConfigureRestTestClient
@Import({
        TestcontainersConfiguration.class,
        CustomerEventKafkaTest.TestConsumerConfiguration.class
})
@SpringBootTest(
        properties = {
                "spring.cloud.function.definition=testConsumer",
                "spring.cloud.stream.bindings.testConsumer-in-0.destination=customer-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
public class CustomerEventKafkaTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue;

    @Test
    public void genreUpdatedEvent() throws InterruptedException {
        GenreUpdateRequest request = new GenreUpdateRequest("Thriller");
        testClient.patch()
                .uri("/api/customers/2/genre")
                .body(request)
                .exchange()
                .expectStatus().isNoContent();

        // validate the event
        var message = queue.poll(5, TimeUnit.SECONDS);
        Assertions.assertNotNull(message);
        var event =  message.getPayload();
        Assertions.assertEquals(2, message.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        Assertions.assertEquals(2,  event.customerId());
        Assertions.assertEquals("Thriller",  event.favoriteGenre());
    }

    @TestConfiguration
    static class TestConsumerConfiguration{

        @Bean
        public BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue(){
            return new LinkedBlockingQueue<>();
        }

        @Bean
        Consumer<Message<CustomerGenreUpdatedEvent>> testConsumer(BlockingQueue<Message<CustomerGenreUpdatedEvent>> queue){
            return queue::add;
        }

    }

}
