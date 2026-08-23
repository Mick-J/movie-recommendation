package com.mick.movie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;

@AutoConfigureRestTestClient
@SpringBootTest(properties = "app.import-movies=false")
@Sql(value = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class MovieApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    public void movieDetails() {
        testClient.get()
                .uri("/api/movies/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Inception")
                .jsonPath("$.runtime").isEqualTo(150);
    }

    @Test
    public void movieNotFound() {
        testClient.get()
                .uri("/api/movies/10")
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
