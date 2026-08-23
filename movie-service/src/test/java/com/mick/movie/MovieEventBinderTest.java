package com.mick.movie;

import com.mick.movie.dto.MovieDetails;
import com.mick.netflux.events.MovieAddedEvent;
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


@EnableTestBinder
@AutoConfigureRestTestClient
@SpringBootTest(properties = "app.import-movies=false")
public class MovieEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void movieAddedEvent() {
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
        var message = this.outputDestination.receive(1000, "movie-events");
        var event = JsonMapper.shared().readValue(message.getPayload(), MovieAddedEvent.class);
        Assertions.assertNotNull(event);
        Assertions.assertEquals(response.id(), event.movieId());
        Assertions.assertEquals(response.id(), message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        Assertions.assertEquals("Shadows in Paradise", event.title());
    }

}
