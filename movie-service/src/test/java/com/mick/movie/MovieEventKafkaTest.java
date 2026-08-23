package com.mick.movie;

import com.mick.movie.dto.MovieDetails;
import com.mick.netflux.events.MovieAddedEvent;
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
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AutoConfigureRestTestClient
@Import({
        TestcontainersConfiguration.class,
        MovieEventKafkaTest.TestConsumerConfiguration.class
})
@SpringBootTest(
        properties = {
                "app.import-movies=false",
                "spring.cloud.function.definition=testConsumer",
                "spring.cloud.stream.bindings.testConsumer-in-0.destination=movie-events",
                "spring.cloud.stream.kafka.binder.consumer-properties.key.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer",
                "spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest"
        }
)
public class MovieEventKafkaTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private BlockingQueue<Message<MovieAddedEvent>> queue;

    @Test
    public void movieAddedEvent() throws InterruptedException {
        var json = """
              {"title":"Shadows in Paradise","voteAverage":7.199,"voteCount":281,"releaseDate":"1986-10-17","revenue":0,"runtime":74,"backdropPath":"/l94l89eMmFKh7na2a1u5q67VgNx.jpg","budget":0,"homepage":"","overview":"Nikander, a rubbish collector and would-be entrepreneur finds his plans for success dashed when his business associate dies. One evening, he meets Ilona, a down-on-her luck cashier in a local supermarket—and, falteringly, a bond begins to develop between them.","popularity":5.946,"posterPath":"/nj01hspawPof0mJmlgfjuLyJuRN.jpg","genres":["Drama","Comedy","Romance"]}
              """;

        var request = JsonMapper.shared().readValue(json, MovieDetails.class);
        MovieDetails response = testClient.post()
                .uri("/api/movies")
                .body(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(MovieDetails.class)
                .getResponseBody();

        // validate the movie response
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals("Shadows in Paradise", response.title());

        // validate movie added event
        var message = this.queue.poll(5, TimeUnit.SECONDS);
        Assertions.assertNotNull(message);
        var event = message.getPayload();

        Assertions.assertEquals(response.id(), message.getHeaders().get(KafkaHeaders.RECEIVED_KEY, Integer.class));
        Assertions.assertEquals(response.id(), event.movieId());
        Assertions.assertEquals("Shadows in Paradise", event.title());
    }

    @TestConfiguration
    static class TestConsumerConfiguration{

        @Bean
        public BlockingQueue<Message<MovieAddedEvent>> queue(){
            return new LinkedBlockingQueue<>();
        }

        @Bean
        Consumer<Message<MovieAddedEvent>> testConsumer(BlockingQueue<Message<MovieAddedEvent>> queue){
            return queue::add;
        }

    }

}
