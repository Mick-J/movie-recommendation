package com.mick.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
public class CustomerApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    public void customerDetails() {
        testClient.get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.favoriteGenre").isEqualTo("Action");

    }

    @Test
    public void customerNotFound() {
        testClient.get()
                .uri("/api/customers/10")
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
